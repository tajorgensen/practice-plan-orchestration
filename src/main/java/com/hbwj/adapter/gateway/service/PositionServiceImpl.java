package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.PositionRepository;
import com.hbwj.adapter.gateway.repository.SportRepository;
import com.hbwj.adapter.gateway.repository.entity.PositionEntity;
import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import com.hbwj.domain.model.Position;
import com.hbwj.domain.port.PositionService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final SportRepository sportRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, SportRepository sportRepository) {
        this.positionRepository = positionRepository;
        this.sportRepository = sportRepository;
    }

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Position> getPositionsBySportId(Long sportId) {
        return positionRepository.findBySportId(sportId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Position getPositionById(Long id) {
        PositionEntity position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
        return mapTo(position);
    }

    @Override
    @Transactional
    public Position createPosition(Position domain) {
        SportEntity sport = sportRepository.findById(domain.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + domain.getSportId()));

        PositionEntity position = new PositionEntity();
        position.setName(position.getName());
        position.setDescription(position.getDescription());
        position.setSport(sport);

        PositionEntity savedPosition = positionRepository.save(position);
        return mapTo(savedPosition);
    }

    @Override
    @Transactional
    public Position updatePosition(Long id, Position domain) {
        PositionEntity position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));

        SportEntity sport = sportRepository.findById(domain.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + domain.getSportId()));

        position.setName(position.getName());
        position.setDescription(position.getDescription());
        position.setSport(sport);

        PositionEntity updatedPosition = positionRepository.save(position);
        return mapTo(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Position not found with id: " + id);
        }
        positionRepository.deleteById(id);
    }

    private Position mapTo(PositionEntity position) {
        Position dto = new Position();
        dto.setId(position.getId());
        dto.setName(position.getName());
        dto.setDescription(position.getDescription());
        dto.setSportId(position.getSport().getId());
        dto.setSportName(position.getSport().getName());
        dto.setCreatedAt(position.getCreatedAt());
        dto.setUpdatedAt(position.getUpdatedAt());
        return dto;
    }
}
