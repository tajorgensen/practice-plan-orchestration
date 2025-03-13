package com.hbwj.domain.useCase;

import com.hbwj.adapter.model.PracticePlanGenerateRequest;
import com.hbwj.adapter.model.PracticePlanSection;
import com.hbwj.adapter.model.Station;
import com.hbwj.adapter.model.StationGroup;
import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.FocusArea;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.port.FocusAreaService;
import com.hbwj.domain.port.PositionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper class responsible for creating different sections of the practice plan.
 */
@Component
public class PracticePlanSectionGenerator {

    /**
     * Create a water break section
     */
    public PracticePlanSection createWaterBreakSection() {
        PracticePlanSection waterBreak = new PracticePlanSection();
        waterBreak.setSectionType("Water Break");
        waterBreak.setDurationMinutes(5);
        waterBreak.setCoachingPoints("Allow players to hydrate and recover");
        waterBreak.setConcurrent(false);
        return waterBreak;
    }

    /**
     * Create the warmup section for the practice plan
     */
    public PracticePlanSection createWarmupSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds,
            PracticePlanDrillSelector drillSelector) {

        int warmupDuration = requestDto.getWarmupDurationMinutes();

        // Find suitable warmup drills (prioritize fundamentals focus area)
        List<Drill> warmupDrills = availableDrills.stream()
                .filter(d -> d.getFocusAreaName() != null && d.getFocusAreaName().equalsIgnoreCase("Fundamentals"))
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= warmupDuration)
                .sorted(Comparator.comparing(Drill::getDifficultyLevel))
                .collect(Collectors.toList());

        // If no fundamentals drills available, use any drill that fits time constraint
        if (warmupDrills.isEmpty()) {
            warmupDrills = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= warmupDuration)
                    .sorted(Comparator.comparing(Drill::getDifficultyLevel))
                    .collect(Collectors.toList());
        }

        if (!warmupDrills.isEmpty()) {
            Drill warmupDrill = drillSelector.selectRandomDrill(warmupDrills, usedDrillIds);
            if (warmupDrill != null) {
                usedDrillIds.add(warmupDrill.getId());

                PracticePlanSection warmupSection = new PracticePlanSection();
                warmupSection.setSectionType("Warmup");
                warmupSection.setDurationMinutes(warmupDuration);
                warmupSection.setDrill(warmupDrill);
                warmupSection.setCoachingPoints(drillSelector.generateCoachingPoints(warmupDrill));
                warmupSection.setConcurrent(false);

                return warmupSection;
            }
        }

        return null;
    }

    /**
     * Create a concurrent stations section with rotations
     */
    public PracticePlanSection createStationsSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds,
            PracticePlanDrillSelector drillSelector) {

        int totalStationTime = requestDto.getStationTotalDurationMinutes();
        int stationRotationTime = requestDto.getStationRotationMinutes();
        int numStations = requestDto.getCoachingStations();

        // Calculate how many full rotations we can have
        int numFullRotations = totalStationTime / (stationRotationTime * numStations);
        int remainingTime = totalStationTime % (stationRotationTime * numStations);
        int totalRotations = remainingTime > 0 ? numFullRotations + 1 : numFullRotations;

        // Create a section for stations with concurrent flag
        PracticePlanSection stationsSection = new PracticePlanSection();
        stationsSection.setSectionType("Stations");
        stationsSection.setDurationMinutes(totalStationTime);
        stationsSection.setConcurrent(true);
        stationsSection.setStationGroups(new ArrayList<>());

        // Create drills for each station
        List<Drill> stationDrills = new ArrayList<>();
        for (int i = 0; i < numStations; i++) {
            // Find suitable drills for this station that haven't been used yet
            List<Drill> stationDrillCandidates = availableDrills.stream()
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= stationRotationTime * 2) // Allow some flexibility
                    .filter(d -> !usedDrillIds.contains(d.getId())) // Avoid duplicate drills
                    .collect(Collectors.toList());

            if (!stationDrillCandidates.isEmpty()) {
                Drill stationDrill = drillSelector.selectBestDrill(stationDrillCandidates, null, usedDrillIds);
                if (stationDrill != null) {
                    usedDrillIds.add(stationDrill.getId());
                    stationDrills.add(stationDrill);
                    continue;
                }
            }

            // If we can't find an unused drill, create a generic station drill
            Drill genericDrill = new Drill();
            genericDrill.setId(-1L * (i + 1)); // Use negative IDs for synthetic drills
            genericDrill.setName("Station " + (i + 1) + " Activities");
            genericDrill.setDescription("General skill development activities for this station");
            genericDrill.setInstructions("Coach should lead players through a series of skill-building exercises appropriate for this station.");
            genericDrill.setDurationMinutes(stationRotationTime);
            stationDrills.add(genericDrill);
        }

        // Create rotation groups
        for (int rotation = 0; rotation < totalRotations; rotation++) {
            StationGroup rotationGroup = new StationGroup();
            rotationGroup.setRotationNumber(rotation + 1);

            // For the last rotation, we might have less time if there's a remainder
            if (rotation == numFullRotations && remainingTime > 0) {
                rotationGroup.setDurationMinutes(remainingTime);
            } else {
                rotationGroup.setDurationMinutes(stationRotationTime * numStations);
            }

            List<Station> stations = new ArrayList<>();

            // For each station in this rotation
            for (int stationIdx = 0; stationIdx < numStations; stationIdx++) {
                Station station = new Station();
                station.setStationNumber(stationIdx + 1);
                station.setStationName("Station " + (stationIdx + 1)); // Standard station naming

                // Calculate which drill this station should use in this rotation
                // This creates the rotation pattern
                int drillIndex = (stationIdx + rotation) % numStations;
                station.setDrill(stationDrills.get(drillIndex));
                station.setCoachingPoints(
                        "Focus on proper technique. Group " + (stationIdx + 1) +
                                " at Station " + (stationIdx + 1) + " for Rotation " + (rotation + 1));

                stations.add(station);
            }

            rotationGroup.setStations(stations);
            stationsSection.getStationGroups().add(rotationGroup);
        }

        return stationsSection;
    }

    /**
     * Create a position group section for a specific focus area
     */
    public PracticePlanSection createFocusAreaPositionGroupSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds,
            String focusAreaName,
            FocusAreaService focusAreaService,
            PositionService positionService,
            PracticePlanDrillSelector drillSelector) {

        // Find the focus area by name
        List<FocusArea> focusAreas = focusAreaService.getAllFocusAreas();
        FocusArea targetFocusArea = focusAreas.stream()
                .filter(fa -> fa.getName().equalsIgnoreCase(focusAreaName))
                .findFirst()
                .orElse(null);

        if (targetFocusArea == null) {
            return null;
        }

        String positionType;
        if (focusAreaName.equalsIgnoreCase("Offense")) {
            positionType = "OFFENSE";
        } else if (focusAreaName.equalsIgnoreCase("Defense")) {
            positionType = "DEFENSE";
        } else {
            positionType = null;
        }

        // Get positions filtered by type if applicable
        List<Position> relevantPositions;
        if (positionType != null) {
            relevantPositions = positionService.getPositionsBySportIdAndPositionType(
                    requestDto.getSportId(), positionType);
        } else {
            relevantPositions = positionService.getPositionsBySportId(requestDto.getSportId());
        }

        if (relevantPositions.isEmpty()) {
            return null;
        }

        int positionGroupDuration = requestDto.getPositionGroupDurationMinutes();

        // Create a position group section (not rotational)
        PracticePlanSection positionSection = new PracticePlanSection();
        positionSection.setSectionType(focusAreaName + " Position Groups");
        positionSection.setDurationMinutes(positionGroupDuration);
        positionSection.setConcurrent(true); // Positions work concurrently, but there's no rotation

        // Create a position group structure (only one group)
        List<StationGroup> positionGroups = new ArrayList<>();
        StationGroup group = new StationGroup();
        group.setRotationNumber(1); // Only one group, no rotation
        group.setDurationMinutes(positionGroupDuration);

        // Filter drills for this focus area
        List<Drill> focusAreaDrills = availableDrills.stream()
                .filter(d -> d.getFocusAreaId().equals(targetFocusArea.getId()))
                .collect(Collectors.toList());

        List<Station> positionStations = createPositionStations(
                relevantPositions,
                focusAreaDrills,
                positionGroupDuration,
                usedDrillIds,
                targetFocusArea.getName(),
                drillSelector);

        // If we couldn't find any drills for positions, create generic ones
        if (positionStations.isEmpty()) {
            positionStations = createGenericPositionStations(relevantPositions, targetFocusArea.getName());
        }

        group.setStations(positionStations);
        positionGroups.add(group);
        positionSection.setStationGroups(positionGroups);

        return positionSection;
    }

    /**
     * Create a position group section for the specified focus area
     */
    public PracticePlanSection createPositionGroupSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds,
            FocusArea focusArea,
            PositionService positionService,
            PracticePlanDrillSelector drillSelector) {

        int positionGroupDuration = requestDto.getPositionGroupDurationMinutes();

        // Get all positions for the sport
        List<Position> allPositions = positionService.getPositionsBySportId(requestDto.getSportId());

        if (allPositions.isEmpty()) {
            return null;
        }

        // Create a concurrent position group section
        PracticePlanSection positionSection = new PracticePlanSection();
        positionSection.setSectionType(focusArea.getName() + " Position Groups");
        positionSection.setDurationMinutes(positionGroupDuration);
        positionSection.setConcurrent(true);

        // Create a station group structure for positions
        List<StationGroup> positionGroups = new ArrayList<>();
        StationGroup group = new StationGroup();
        group.setRotationNumber(1);
        group.setDurationMinutes(positionGroupDuration);

        // Filter drills for this focus area
        List<Drill> focusAreaDrills = availableDrills.stream()
                .filter(d -> d.getFocusAreaId().equals(focusArea.getId()))
                .collect(Collectors.toList());

        List<Station> stations = createPositionStations(
                allPositions,
                focusAreaDrills,
                positionGroupDuration,
                usedDrillIds,
                focusArea.getName(),
                drillSelector);

        // If we couldn't find any drills for positions, create generic ones
        if (stations.isEmpty()) {
            stations = createGenericPositionStations(allPositions, focusArea.getName());
        }

        group.setStations(stations);
        positionGroups.add(group);
        positionSection.setStationGroups(positionGroups);

        return positionSection;
    }

    /**
     * Create the team time section
     */
    public PracticePlanSection createTeamTimeSection(
            PracticePlanGenerateRequest requestDto,
            List<Drill> availableDrills,
            Set<Long> usedDrillIds,
            PracticePlanDrillSelector drillSelector) {

        int teamTimeDuration = requestDto.getTeamTimeDurationMinutes();

        // Return null if no time for team activities
        if (teamTimeDuration <= 0) {
            return null;
        }

        // Find suitable team drills (prioritize drills marked as team activities) that haven't been used
        List<Drill> teamDrills = availableDrills.stream()
                .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() <= teamTimeDuration)
                .filter(d -> !usedDrillIds.contains(d.getId())) // Only consider unused drills
                // Prioritize drills explicitly marked as team activities
                .sorted((d1, d2) -> {
                    boolean d1TeamFocused = Boolean.TRUE.equals(d1.getIsTeamActivity());
                    boolean d2TeamFocused = Boolean.TRUE.equals(d2.getIsTeamActivity());

                    // Fall back to keyword matching if no explicit flag is set
                    if (!d1TeamFocused && !d2TeamFocused) {
                        d1TeamFocused = d1.getDescription() != null &&
                                (d1.getDescription().toLowerCase().contains("team") ||
                                        d1.getDescription().toLowerCase().contains("group") ||
                                        d1.getDescription().toLowerCase().contains("scrimmage"));
                        d2TeamFocused = d2.getDescription() != null &&
                                (d2.getDescription().toLowerCase().contains("team") ||
                                        d2.getDescription().toLowerCase().contains("group") ||
                                        d2.getDescription().toLowerCase().contains("scrimmage"));
                    }

                    if (d1TeamFocused && !d2TeamFocused) return -1;
                    if (!d1TeamFocused && d2TeamFocused) return 1;
                    return 0;
                })
                .collect(Collectors.toList());

        if (!teamDrills.isEmpty()) {
            Drill teamDrill = drillSelector.selectRandomDrill(teamDrills, usedDrillIds);
            if (teamDrill != null) {
                usedDrillIds.add(teamDrill.getId());

                PracticePlanSection teamSection = new PracticePlanSection();
                teamSection.setSectionType("Team Time");
                teamSection.setDurationMinutes(teamTimeDuration);
                teamSection.setDrill(teamDrill);
                teamSection.setCoachingPoints("Focus on team coordination and game situations");
                teamSection.setConcurrent(false);

                return teamSection;
            }
        }

        // If no suitable unused drills, create a generic team time section
        PracticePlanSection teamSection = new PracticePlanSection();
        teamSection.setSectionType("Team Time");
        teamSection.setDurationMinutes(teamTimeDuration);

        // Create a generic team drill
        Drill genericTeamDrill = new Drill();
        genericTeamDrill.setId(-100L); // Use a negative ID to avoid conflicts
        genericTeamDrill.setName("Team Scrimmage");
        genericTeamDrill.setDescription("Full team scrimmage to apply skills learned during practice");
        genericTeamDrill.setInstructions("Divide team into two groups. Run a controlled scrimmage focusing on implementing concepts covered earlier in practice. Coaches should provide immediate feedback and make teaching moments when necessary.");
        genericTeamDrill.setDurationMinutes(teamTimeDuration);
        genericTeamDrill.setIsTeamActivity(true);

        teamSection.setDrill(genericTeamDrill);
        teamSection.setCoachingPoints("Focus on team coordination, game situations, and applying skills learned in practice");
        teamSection.setConcurrent(false);

        return teamSection;
    }

    /**
     * Helper method to create position-specific stations for a given focus area
     */
    private List<Station> createPositionStations(
            List<Position> allPositions,
            List<Drill> focusAreaDrills,
            int duration,
            Set<Long> usedDrillIds,
            String focusAreaName,
            PracticePlanDrillSelector drillSelector) {

        List<Station> stations = new ArrayList<>();
        int stationNumber = 1;

        // For each position, try to find a drill in this focus area
        for (Position position : allPositions) {
            // Find drills for this position in the focus area that are at least 5 minutes
            List<Drill> positionDrills = focusAreaDrills.stream()
                    .filter(d -> d.getPositionIds().contains(position.getId()))
                    .filter(d -> d.getDurationMinutes() != null && d.getDurationMinutes() >= 5 && d.getDurationMinutes() <= duration)
                    .filter(d -> !usedDrillIds.contains(d.getId()))
                    .collect(Collectors.toList());

            if (!positionDrills.isEmpty()) {
                // Select only one drill for this position group
                Drill positionDrill = drillSelector.selectRandomDrill(positionDrills, usedDrillIds);
                if (positionDrill != null) {
                    usedDrillIds.add(positionDrill.getId());

                    Station station = new Station();
                    station.setStationNumber(stationNumber++);
                    station.setStationName(position.getName()); // Use position name for station name
                    station.setDrill(positionDrill);
                    station.setCoachingPoints("All " + position.getName() + "s focus on this " + focusAreaName +
                            " drill as a single group. Provide individual feedback and coaching.");

                    stations.add(station);
                }
            } else {
                // If no suitable drill found, create a single generic drill for this position
                Drill genericDrill = drillSelector.findGenericDrillForPosition(position, focusAreaDrills, duration, usedDrillIds);
                if (genericDrill != null) {
                    usedDrillIds.add(genericDrill.getId());

                    Station station = new Station();
                    station.setStationNumber(stationNumber++);
                    station.setStationName(position.getName()); // Use position name for station name
                    station.setDrill(genericDrill);
                    station.setCoachingPoints("All " + position.getName() + "s work together as one group on this drill " +
                            "with focus on " + focusAreaName + " skills.");

                    stations.add(station);
                } else {
                    // Create a fully synthetic position drill
                    Drill syntheticDrill = new Drill();
                    syntheticDrill.setId(-1L * stationNumber); // Use negative IDs for synthetic drills
                    syntheticDrill.setName(position.getName() + " " + focusAreaName + " Skills");
                    syntheticDrill.setDescription("Position-specific " + focusAreaName + " skills for " + position.getName());
                    syntheticDrill.setInstructions("All " + position.getName() + "s work on key " + focusAreaName +
                            " techniques relevant to their position.");
                    syntheticDrill.setDurationMinutes(duration); // Use full position group duration

                    Station station = new Station();
                    station.setStationNumber(stationNumber++);
                    station.setStationName(position.getName()); // Use position name for station name
                    station.setDrill(syntheticDrill);
                    station.setCoachingPoints("Have all " + position.getName() + "s focus on " +
                            focusAreaName + " skills as a single position group.");

                    stations.add(station);
                }
            }
        }

        return stations;
    }

    /**
     * Create stations with generic position descriptions when no suitable drills are found
     */
    private List<Station> createGenericPositionStations(List<Position> allPositions, String focusAreaName) {
        List<Station> stations = new ArrayList<>();
        int stationNumber = 1;

        for (Position position : allPositions) {
            // Create a generic drill for this position for the entire duration
            Drill genericDrill = new Drill();
            genericDrill.setId(-1L * stationNumber); // Use negative IDs for synthetic drills
            genericDrill.setName(position.getName() + " " + focusAreaName + " Skills");
            genericDrill.setDescription("Position-specific " + focusAreaName + " skills for all " + position.getName() + "s");
            genericDrill.setInstructions("Have all " + position.getName() + "s focus on key " + focusAreaName +
                    " techniques relevant to their position as a single group.");
            genericDrill.setDurationMinutes(15); // Reasonable duration for position work

            Station station = new Station();
            station.setStationNumber(stationNumber++);
            station.setStationName(position.getName()); // Use position name for station name
            station.setDrill(genericDrill);
            station.setCoachingPoints("All " + position.getName() + "s work together as one group on " +
                    focusAreaName + " skills specific to their position.");

            stations.add(station);
        }

        return stations;
    }

    /**
     * Insert additional water breaks at 45-minute intervals
     */
    public List<PracticePlanSection> insertAdditionalWaterBreaks(List<PracticePlanSection> sections) {
        List<PracticePlanSection> sectionsWithBreaks = new ArrayList<>();
        int runningTime = 0;

        for (int i = 0; i < sections.size(); i++) {
            PracticePlanSection section = sections.get(i);

            // Skip water breaks that are already in the plan
            if (section.getSectionType().equals("Water Break")) {
                sectionsWithBreaks.add(section);
                runningTime = 0; // Reset the running time after a break
                continue;
            }

            // If adding this section would put us over 45 minutes, add a water break first
            // But don't add another break if we just had one
            if (runningTime + section.getDurationMinutes() > 45 && runningTime > 0) {
                // Add a water break
                PracticePlanSection waterBreak = createWaterBreakSection();
                sectionsWithBreaks.add(waterBreak);
                runningTime = 0; // Reset the running time
            }

            // Add the current section
            sectionsWithBreaks.add(section);
            runningTime += section.getDurationMinutes();
        }

        return sectionsWithBreaks;
    }
}