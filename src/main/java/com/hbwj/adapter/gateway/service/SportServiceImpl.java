package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.SportRepository;
import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import com.hbwj.domain.model.Sport;
import com.hbwj.domain.port.SportService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    @Autowired
    public SportServiceImpl(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Override
    public List<Sport> getAllSports() {
        return sportRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Sport getSportById(Long id) {
        SportEntity sport = sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + id));
        return mapTo(sport);
    }

    @Override
    @Transactional
    public Sport createSport(Sport domain) {
        SportEntity sport = new SportEntity();
        sport.setName(sport.getName());
        sport.setDescription(sport.getDescription());

        SportEntity savedSport = sportRepository.save(sport);
        return mapTo(savedSport);
    }

    @Override
    @Transactional
    public Sport updateSport(Long id, Sport domain) {
        SportEntity sport = sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found with id: " + id));

        sport.setName(domain.getName());
        sport.setDescription(domain.getDescription());

        SportEntity updatedSport = sportRepository.save(sport);
        return mapTo(updatedSport);
    }

    @Override
    @Transactional
    public void deleteSport(Long id) {
        if (!sportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sport not found with id: " + id);
        }
        sportRepository.deleteById(id);
    }

    private Sport mapTo(SportEntity sport) {
        Sport dto = new Sport();
        dto.setId(sport.getId());
        dto.setName(sport.getName());
        dto.setDescription(sport.getDescription());
        dto.setCreatedAt(sport.getCreatedAt());
        dto.setUpdatedAt(sport.getUpdatedAt());
        return dto;
    }
}

