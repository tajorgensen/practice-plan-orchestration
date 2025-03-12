-- V2025_03_11_99__consolidated_seed_data.sql
-- Seed data for initial application setup

-- Insert Focus Areas (these are fixed)
INSERT INTO focus_areas (name, description) VALUES
                                                ('Fundamentals', 'Basic skills and techniques that form the foundation of the sport'),
                                                ('Offense', 'Skills and strategies focused on scoring and advancing'),
                                                ('Defense', 'Skills and strategies focused on preventing the opponent from scoring');

-- Insert sample sports
INSERT INTO sports (name, description) VALUES
                                           ('Basketball', 'A team sport played with a ball and hoop'),
                                           ('Football', 'A team sport played with an oval ball and goalposts'),
                                           ('Soccer', 'A team sport played with a round ball and goals'),
                                           ('Baseball', 'A bat-and-ball game played between two teams');

-- Insert sample positions for Basketball
INSERT INTO positions (sport_id, name, description) VALUES
                                                        (1, 'Point Guard', 'Primary ball handler and playmaker'),
                                                        (1, 'Shooting Guard', 'Focused on scoring from the perimeter'),
                                                        (1, 'Small Forward', 'Versatile position that can score inside and outside'),
                                                        (1, 'Power Forward', 'Interior player focused on rebounding and inside scoring'),
                                                        (1, 'Center', 'Typically the tallest player, protects the rim and scores inside');

-- Insert sample positions for Football
INSERT INTO positions (sport_id, name, description) VALUES
                                                        (2, 'Quarterback', 'Leader of the offense who handles the ball on most plays'),
                                                        (2, 'Running Back', 'Carries the ball on running plays'),
                                                        (2, 'Wide Receiver', 'Catches passes from the quarterback'),
                                                        (2, 'Tight End', 'Hybrid position that blocks and catches passes'),
                                                        (2, 'Offensive Line', 'Protects the quarterback and creates running lanes'),
                                                        (2, 'Defensive Line', 'Rushes the quarterback and stops the run'),
                                                        (2, 'Linebacker', 'Second level of defense, versatile in run support and pass coverage'),
                                                        (2, 'Cornerback', 'Defends against passes'),
                                                        (2, 'Safety', 'Last line of defense, provides deep pass coverage and run support'),
                                                        (2, 'Kicker', 'Responsible for kickoffs and field goals'),
                                                        (2, 'Punter', 'Kicks the ball to change field position on fourth down'),
                                                        (2, 'Long Snapper', 'Delivers the ball to punter or holder on special teams plays');

-- Insert sample categories for Basketball - Fundamentals
INSERT INTO categories (sport_id, focus_area_id, name, description) VALUES
                                                                        (1, 1, 'Ball Handling', 'Skills related to controlling the basketball'),
                                                                        (1, 1, 'Shooting', 'Skills related to shooting the basketball accurately'),
                                                                        (1, 1, 'Passing', 'Skills related to passing the basketball effectively'),
                                                                        (1, 1, 'Footwork', 'Skills related to proper foot positioning and movement');

-- Insert categories for Football (Fundamentals)
INSERT INTO categories (sport_id, focus_area_id, name, description) VALUES
                                                                        (2, 1, 'Blocking', 'Techniques to create running lanes and protect the quarterback'),
                                                                        (2, 1, 'Ball Handling', 'Skills for carrying, exchanging, and protecting the football'),
                                                                        (2, 1, 'Passing', 'Techniques for throwing accurate and catchable passes'),
                                                                        (2, 1, 'Receiving', 'Skills for catching passes and securing the football'),
                                                                        (2, 1, 'Tackling', 'Safe and effective techniques for bringing down ball carriers');

-- Insert categories for Football (Offense)
INSERT INTO categories (sport_id, focus_area_id, name, description) VALUES
                                                                        (2, 2, 'Run Game', 'Offensive plays designed to advance the ball by rushing'),
                                                                        (2, 2, 'Pass Game', 'Offensive plays designed to advance the ball through the air'),
                                                                        (2, 2, 'Play-Action', 'Deceptive plays that simulate runs before passing'),
                                                                        (2, 2, 'Screen Game', 'Short passing plays with blockers set up downfield'),
                                                                        (2, 2, 'Red Zone', 'Specialized plays for scoring inside the 20-yard line');

-- Insert categories for Football (Defense)
INSERT INTO categories (sport_id, focus_area_id, name, description) VALUES
                                                                        (2, 3, 'Run Defense', 'Strategies and techniques to stop the opponent''s running game'),
                                                                        (2, 3, 'Pass Coverage', 'Techniques for defending against passes'),
                                                                        (2, 3, 'Pass Rush', 'Techniques for pressuring the quarterback'),
                                                                        (2, 3, 'Blitz Packages', 'Defensive plays that send extra pass rushers'),
                                                                        (2, 3, 'Takeaways', 'Techniques for creating turnovers through interceptions and fumbles');

-- Insert sample subcategories for Basketball - Ball Handling
INSERT INTO subcategories (category_id, name, description) VALUES
                                                               (1, 'Dribbling', 'Control of the basketball while moving'),
                                                               (1, 'Ball Protection', 'Protecting the ball from defenders'),
                                                               (1, 'Hand-Eye Coordination', 'Coordination between hands and eyes');

-- Insert sample subcategories for Basketball - Shooting
INSERT INTO subcategories (category_id, name, description) VALUES
                                                               (2, 'Form', 'Proper shooting form and technique'),
                                                               (2, 'Free Throws', 'Shooting from the free throw line'),
                                                               (2, 'Three-Point Shooting', 'Shooting from beyond the three-point line');

-- Insert subcategories for Football - Blocking (select category_id 5)
INSERT INTO subcategories (category_id, name, description) VALUES
                                                               (5, 'Pass Blocking', 'Techniques for protecting the quarterback during passing plays'),
                                                               (5, 'Run Blocking', 'Techniques for creating running lanes for ball carriers'),
                                                               (5, 'Pull Blocking', 'Techniques for linemen to move laterally and lead running plays'),
                                                               (5, 'Screen Blocking', 'Techniques for setting up downfield blocks on screen passes');

-- Insert subcategories for Football - Ball Handling (select category_id 6)
INSERT INTO subcategories (category_id, name, description) VALUES
                                                               (6, 'Handoffs', 'Proper technique for transferring the ball to a runner'),
                                                               (6, 'Ball Security', 'Techniques for protecting the ball and preventing fumbles'),
                                                               (6, 'Quarterback-Center Exchange', 'Proper technique for snapping and receiving the ball');

-- Insert subcategories for Football - Passing (select category_id 7)
INSERT INTO subcategories (category_id, name, description) VALUES
                                                               (7, 'Throwing Mechanics', 'Proper technique for throwing a football'),
                                                               (7, 'Reading Defenses', 'Skills for identifying defensive coverages and adjusting accordingly'),
                                                               (7, 'Accuracy Training', 'Drills to improve passing precision and ball placement');

-- Insert sample KPIs for Basketball - Dribbling
INSERT INTO kpis (subcategory_id, name, description, measurement_unit, target_value) VALUES
                                                                                         (1, 'Speed Dribble', 'How fast a player can dribble from baseline to baseline', 'seconds', 4.5),
                                                                                         (1, 'Control Dribble', 'How many successful dribbles in 30 seconds with defensive pressure', 'count', 25),
                                                                                         (1, 'Crossover Efficiency', 'Success rate of crossover moves against defenders', 'percentage', 80);

-- Insert sample KPIs for Basketball - Shooting Form
INSERT INTO kpis (subcategory_id, name, description, measurement_unit, target_value) VALUES
                                                                                         (4, 'Release Speed', 'Time from catch to release', 'seconds', 0.8),
                                                                                         (4, 'Arc Height', 'Optimal arc height for shooting', 'degrees', 45),
                                                                                         (4, 'Follow Through', 'Proper wrist snap and follow through after release', 'score 1-10', 8);

-- Insert sample equipment
INSERT INTO equipment (name, description) VALUES
                                              ('Basketball', 'Regulation size and weight basketball'),
                                              ('Cones', 'Training cones for marking drill areas'),
                                              ('Agility Ladder', 'Ladder for footwork drills'),
                                              ('Resistance Bands', 'Bands to add resistance to movement'),
                                              ('Rebounding Net', 'Net that returns basketball to the shooter'),
                                              ('Free Throw Line Marker', 'Marker for proper foot placement at free throw line'),
                                              ('Ball Return System', 'Automated system that returns balls to the shooter');

-- Insert football equipment
INSERT INTO equipment (name, description) VALUES
                                              ('Football', 'Regulation size and weight football'),
                                              ('Blocking Sled', 'Training equipment for blocking technique'),
                                              ('Blocking Pads', 'Hand-held pads for blocking practice'),
                                              ('Tackle Dummies', 'Padded equipment for tackling practice'),
                                              ('Hand Shields', 'Padded shields for contact drills'),
                                              ('Ball-on-a-Stick', 'Tool for practicing ball stripping and takeaways'),
                                              ('Passing Nets', 'Target nets for passing accuracy training');

-- Insert sample drills for Basketball
INSERT INTO drills (name, description, instructions, duration_minutes, difficulty_level, focus_area_id) VALUES
                                                                                                            ('Figure 8 Dribbling', 'Dribbling in a figure 8 pattern to improve ball control',
                                                                                                             'Set up two cones about 3 feet apart. Dribble around the cones in a figure 8 pattern, alternating hands as you move from one cone to the other. Focus on keeping the ball low and maintaining control.',
                                                                                                             5, 'Beginner', 1),

                                                                                                            ('Two-Ball Dribbling', 'Dribbling two basketballs simultaneously to improve coordination',
                                                                                                             'Using two basketballs, dribble both simultaneously at the same height. Progress to alternating heights (one high, one low). Then try dribbling while walking, then jogging.',
                                                                                                             10, 'Intermediate', 1),

                                                                                                            ('Form Shooting Close Range', 'Close range shooting focusing on proper form',
                                                                                                             'Stand 3-5 feet from the basket. Focus on proper shooting form: balanced stance, elbow under the ball, wrist snap, and follow through. Make 10 shots with perfect form before stepping back.',
                                                                                                             15, 'Beginner', 1),

                                                                                                            ('Free Throw Routine', 'Developing a consistent free throw routine',
                                                                                                             'Establish a consistent pre-shot routine. Take 10 deep breaths, bounce the ball 3 times, spin it in your hands, focus on the rim, bend knees, and shoot with proper form. Shoot 25 free throws, focusing on consistency.',
                                                                                                             20, 'Intermediate', 1),

                                                                                                            ('3-Point Circuit', 'Shooting from 5 positions around the 3-point line',
                                                                                                             'Set up at 5 spots around the 3-point line. Take 5 shots from each position, rotating clockwise. Track makes and aim for improvement each session.',
                                                                                                             15, 'Advanced', 2);

-- Insert football drills (Fundamentals)
INSERT INTO drills (name, description, instructions, duration_minutes, difficulty_level, focus_area_id) VALUES
                                                                                                            ('QB Footwork Progression', 'Develop quarterback dropback and setup technique',
                                                                                                             'Set up 5 cones in a line with 2 yards between each. QB practices 3-step, 5-step, and 7-step drops, focusing on proper footwork, balance, and setup position. After each drop, QB sets feet and simulates throwing motion. Emphasize quick feet, proper weight transfer, and maintaining proper throwing posture.',
                                                                                                             15, 'Intermediate', 1),

                                                                                                            ('Pass Protection Footwork', 'Develop offensive line pass blocking footwork',
                                                                                                             'Set up in a line with each lineman in proper stance. On coach''s signal, linemen take a kick-step back at 45-degree angle, maintaining base and balance. Progress to 3-step protection sets, focusing on maintaining leverage and balance. Coach can add hand shields to simulate pass rushers. Emphasize quick feet, good base, and proper hand placement.',
                                                                                                             15, 'Intermediate', 1),

                                                                                                            ('Receiver Route Tree', 'Practice running precise routes for receivers',
                                                                                                             'Set up cones marking the starting point and each break point for routes. Practice each of the 9 basic routes: flat, slant, quick out, curl, comeback, dig (in), post, corner, and go. Focus on sharp breaks, proper footwork, and maintaining speed through cuts. Coach can throw passes to complete each route.',
                                                                                                             20, 'Intermediate', 1),

                                                                                                            ('Form Tackling Circuit', 'Practice proper tackling technique with safety emphasis',
                                                                                                             'Set up 4 stations: 1) Angle tackles on a moving target, 2) Head-on tackles against a tackle dummy, 3) Open-field breakdown and tackle, 4) Gang tackling with teammates. Players rotate through stations, spending 4 minutes at each. Emphasize proper head position (head across the body), wrapping with arms, driving with legs, and safe technique.',
                                                                                                             20, 'Beginner', 1);

-- Insert football drills (Offense)
INSERT INTO drills (name, description, instructions, duration_minutes, difficulty_level, focus_area_id) VALUES
                                                                                                            ('Inside Zone Run Install', 'Install and practice inside zone running play',
                                                                                                             'Set up defense with tackle dummies or coaches. Walk through inside zone blocking assignments for each position. Running backs practice read steps and finding the cutback lane. Progress from walk-through to half-speed, then full-speed reps. Focus on combo blocks, running back reads, and timing.',
                                                                                                             20, 'Intermediate', 2),

                                                                                                            ('Passing Concept Installation', 'Install and practice pass concepts and progressions',
                                                                                                             'Set up defense alignment with cones or coaches. Walk through a passing concept (e.g., Smash, Flood, Mesh) with receivers running routes at walking pace. QB practices progression reads. Progress to half-speed with timing emphasis, then full-speed reps. Focus on route depths, timing, and quarterback progression.',
                                                                                                             25, 'Advanced', 2),

                                                                                                            ('Running Back Screen Development', 'Practice timing and execution of RB screen passes',
                                                                                                             'Set up offensive line, quarterback, and running back. Defensive players or coaches provide token pressure. Practice the three phases of the screen: linemen allow defenders through, quarterback throws over rushers, and linemen release to block downfield. Focus on timing between QB and RB, linemen''s downfield blocking angles, and RB setting up blocks.',
                                                                                                             15, 'Intermediate', 2);

-- Insert football drills (Defense)
INSERT INTO drills (name, description, instructions, duration_minutes, difficulty_level, focus_area_id) VALUES
                                                                                                            ('Linebacker Fit and Fill', 'Practice linebacker run fits and gap responsibilities',
                                                                                                             'Set up offensive line and tight end alignment with tackle dummies or coaches. Linebackers align in proper position. On coach''s signal, offense simulates run blocking while linebackers read and react, filling their assigned gaps. Progress from walk-through to full speed. Focus on proper read steps, taking on blocks, and gap integrity.',
                                                                                                             15, 'Intermediate', 3),

                                                                                                            ('DB Coverage Techniques', 'Practice defensive back coverage techniques',
                                                                                                             'Set up receivers and defensive backs. Practice four techniques: 1) Press coverage with proper jam technique, 2) Off coverage with backpedal and break, 3) Zone coverage with drop and read, 4) Pattern-matching based on receiver releases. Focus on proper footwork, hip flexibility, and eye discipline.',
                                                                                                             20, 'Advanced', 3),

                                                                                                            ('D-Line Pass Rush Techniques', 'Develop defensive line pass rush moves and techniques',
                                                                                                             'Set up offensive linemen with shields or tackle dummies. Defensive linemen practice four main pass rush moves: 1) Bull rush, 2) Rip move, 3) Swim move, 4) Spin move. Each player takes 3-4 reps of each move, focusing on proper technique and timing. Progress to combining moves in sequences. Emphasize get-off speed, hand placement, and finishing to the quarterback.',
                                                                                                             15, 'Intermediate', 3);

-- Connect drills to sports (Basketball)
INSERT INTO drill_sports (drill_id, sport_id) VALUES
                                                  (1, 1), -- Figure 8 Dribbling for Basketball
                                                  (2, 1), -- Two-Ball Dribbling for Basketball
                                                  (3, 1), -- Form Shooting for Basketball
                                                  (4, 1), -- Free Throw Routine for Basketball
                                                  (5, 1); -- 3-Point Circuit for Basketball

-- Connect drills to sports (Football)
INSERT INTO drill_sports (drill_id, sport_id) VALUES
                                                  (6, 2), -- QB Footwork Progression for Football
                                                  (7, 2), -- Pass Protection Footwork for Football
                                                  (8, 2), -- Receiver Route Tree for Football
                                                  (9, 2), -- Form Tackling Circuit for Football
                                                  (10, 2), -- Inside Zone Run Install for Football
                                                  (11, 2), -- Passing Concept Installation for Football
                                                  (12, 2), -- Running Back Screen Development for Football
                                                  (13, 2), -- Linebacker Fit and Fill for Football
                                                  (14, 2), -- DB Coverage Techniques for Football
                                                  (15, 2); -- D-Line Pass Rush Techniques for Football

-- Connect drills to positions (Basketball)
INSERT INTO drill_positions (drill_id, position_id) VALUES
                                                        (1, 1), -- Figure 8 Dribbling for Point Guard
                                                        (1, 2), -- Figure 8 Dribbling for Shooting Guard
                                                        (2, 1), -- Two-Ball Dribbling for Point Guard
                                                        (3, 1), -- Form Shooting for Point Guard
                                                        (3, 2), -- Form Shooting for Shooting Guard
                                                        (3, 3), -- Form Shooting for Small Forward
                                                        (3, 4), -- Form Shooting for Power Forward
                                                        (3, 5), -- Form Shooting for Center
                                                        (4, 1), -- Free Throw Routine for Point Guard
                                                        (4, 2), -- Free Throw Routine for Shooting Guard
                                                        (4, 3), -- Free Throw Routine for Small Forward
                                                        (4, 4), -- Free Throw Routine for Power Forward
                                                        (4, 5), -- Free Throw Routine for Center
                                                        (5, 2), -- 3-Point Circuit for Shooting Guard
                                                        (5, 3); -- 3-Point Circuit for Small Forward

-- Connect drills to positions (Football)
INSERT INTO drill_positions (drill_id, position_id) VALUES
                                                        (6, 6), -- QB Footwork Progression for Quarterback
                                                        (7, 10), -- Pass Protection Footwork for Offensive Line
                                                        (8, 8), -- Receiver Route Tree for Wide Receiver
                                                        (9, 11), -- Form Tackling Circuit for Defensive Line
                                                        (9, 12), -- Form Tackling Circuit for Linebacker
                                                        (9, 13), -- Form Tackling Circuit for Cornerback
                                                        (10, 10), -- Inside Zone Run Install for Offensive Line
                                                        (10, 6), -- Inside Zone Run Install for Quarterback
                                                        (10, 7), -- Inside Zone Run Install for Running Back
                                                        (11, 6), -- Passing Concept Installation for Quarterback
                                                        (11, 8), -- Passing Concept Installation for Wide Receiver
                                                        (12, 6), -- Running Back Screen Development for Quarterback
                                                        (12, 7), -- Running Back Screen Development for Running Back
                                                        (12, 10), -- Running Back Screen Development for Offensive Line
                                                        (13, 12), -- Linebacker Fit and Fill for Linebacker
                                                        (14, 13), -- DB Coverage Techniques for Cornerback
                                                        (15, 11); -- D-Line Pass Rush Techniques for Defensive Line

-- Connect drills to KPIs with impact levels (Basketball)
INSERT INTO drill_kpis (drill_id, kpi_id, impact_level) VALUES
                                                            (1, 1, 7), -- Figure 8 Dribbling impacts Speed Dribble
                                                            (1, 2, 9), -- Figure 8 Dribbling impacts Control Dribble
                                                            (1, 3, 6), -- Figure 8 Dribbling impacts Crossover Efficiency
                                                            (2, 1, 8), -- Two-Ball Dribbling impacts Speed Dribble
                                                            (2, 2, 10), -- Two-Ball Dribbling impacts Control Dribble
                                                            (2, 3, 7), -- Two-Ball Dribbling impacts Crossover Efficiency
                                                            (3, 4, 10), -- Form Shooting impacts Release Speed
                                                            (3, 5, 9), -- Form Shooting impacts Arc Height
                                                            (3, 6, 10), -- Form Shooting impacts Follow Through
                                                            (4, 5, 8), -- Free Throw Routine impacts Arc Height
                                                            (4, 6, 9), -- Free Throw Routine impacts Follow Through
                                                            (5, 4, 7), -- 3-Point Circuit impacts Release Speed
                                                            (5, 5, 6), -- 3-Point Circuit impacts Arc Height
                                                            (5, 6, 7); -- 3-Point Circuit impacts Follow Through

-- Connect drills to equipment (Basketball)
INSERT INTO drill_equipment (drill_id, equipment_id, quantity) VALUES
                                                                   (1, 1, 1), -- Figure 8 Dribbling requires 1 Basketball
                                                                   (1, 2, 2), -- Figure 8 Dribbling requires 2 Cones
                                                                   (2, 1, 2), -- Two-Ball Dribbling requires 2 Basketballs
                                                                   (3, 1, 1), -- Form Shooting requires 1 Basketball
                                                                   (3, 5, 1), -- Form Shooting can use 1 Rebounding Net
                                                                   (4, 1, 1), -- Free Throw Routine requires 1 Basketball
                                                                   (4, 6, 1), -- Free Throw Routine requires 1 Free Throw Line Marker
                                                                   (5, 1, 3), -- 3-Point Circuit requires 3 Basketballs
                                                                   (5, 7, 1); -- 3-Point Circuit can use 1 Ball Return System

-- Connect drills to equipment (Football)
INSERT INTO drill_equipment (drill_id, equipment_id, quantity) VALUES
                                                                   (6, 8, 2), -- QB Footwork Progression requires 2 Footballs
                                                                   (6, 2, 5), -- QB Footwork Progression requires 5 Cones
                                                                   (7, 13, 3), -- Pass Protection Footwork requires 3 Hand Shields
                                                                   (8, 8, 3), -- Receiver Route Tree requires 3 Footballs
                                                                   (8, 2, 10), -- Receiver Route Tree requires 10 Cones
                                                                   (9, 12, 2), -- Form Tackling Circuit requires 2 Tackle Dummies
                                                                   (9, 13, 2), -- Form Tackling Circuit requires 2 Hand Shields
                                                                   (10, 8, 1), -- Inside Zone Run Install requires 1 Football
                                                                   (10, 11, 2), -- Inside Zone Run Install requires 2 Blocking Pads
                                                                   (10, 12, 3), -- Inside Zone Run Install requires 3 Tackle Dummies
                                                                   (11, 8, 3), -- Passing Concept Installation requires 3 Footballs
                                                                   (11, 2, 8), -- Passing Concept Installation requires 8 Cones
                                                                   (12, 8, 2), -- Running Back Screen Development requires 2 Footballs
                                                                   (12, 2, 6), -- Running Back Screen Development requires 6 Cones
                                                                   (13, 13, 3), -- Linebacker Fit and Fill requires 3 Hand Shields
                                                                   (13, 12, 2), -- Linebacker Fit and Fill requires 2 Tackle Dummies
                                                                   (14, 8, 2), -- DB Coverage Techniques requires 2 Footballs
                                                                   (14, 2, 8), -- DB Coverage Techniques requires 8 Cones
                                                                   (14, 3, 1), -- DB Coverage Techniques requires 1 Agility Ladder
                                                                   (15, 13, 4), -- D-Line Pass Rush Techniques requires 4 Hand Shields
                                                                   (15, 11, 2); -- D-Line Pass Rush Techniques requires 2 Blocking Pads

-- Create practice plans
-- Basketball practice plan
INSERT INTO practice_plans (name, description, sport_id, focus_area_id, total_duration_minutes) VALUES
    ('Ball Handling and Shooting Practice', 'A balanced practice focusing on fundamental ball handling and shooting skills', 1, 1, 65);

-- Football practice plan
INSERT INTO practice_plans (name, description, sport_id, focus_area_id, total_duration_minutes) VALUES
    ('Football Fundamentals Practice', 'A balanced practice focusing on fundamental football skills for all position groups', 2, 1, 90);

-- Add drills to the basketball practice plan
INSERT INTO practice_plan_drills (practice_plan_id, drill_id, sequence_order, duration_minutes, notes) VALUES
                                                                                                           (1, 1, 1, 5, 'Warm-up drill to get players comfortable with the ball'),
                                                                                                           (1, 2, 2, 10, 'Challenge players to maintain control with both balls'),
                                                                                                           (1, 3, 3, 15, 'Focus on perfect form, even if shots are close to the basket'),
                                                                                                           (1, 4, 4, 20, 'Have players develop their own personalized routine'),
                                                                                                           (1, 5, 5, 15, 'Track makes/attempts to measure improvement');

-- Add drills to the football practice plan
INSERT INTO practice_plan_drills (practice_plan_id, drill_id, sequence_order, duration_minutes, notes) VALUES
                                                                                                           (2, 6, 1, 15, 'Start with QB fundamentals while players are fresh'),
                                                                                                           (2, 8, 2, 20, 'Focus on precise routes and timing'),
                                                                                                           (2, 7, 3, 15, 'Emphasize proper pass protection technique'),
                                                                                                           (2, 9, 4, 20, 'Finish with fundamental tackling practice');