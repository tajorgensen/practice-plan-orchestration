package com.hbwj.adapter.gateway.service;

import com.hbwj.adapter.gateway.repository.FocusAreaRepository;
import com.hbwj.adapter.gateway.repository.entity.FocusAreaEntity;
import com.hbwj.domain.model.FocusArea;
import com.hbwj.domain.port.FocusAreaService;
import com.hbwj.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusAreaServiceImpl implements FocusAreaService {

    private final FocusAreaRepository focusAreaRepository;

    @Autowired
    public FocusAreaServiceImpl(FocusAreaRepository focusAreaRepository) {
        this.focusAreaRepository = focusAreaRepository;
    }

    @Override
    public List<FocusArea> getAllFocusAreas() {
        return focusAreaRepository.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public FocusArea getFocusAreaById(Long id) {
        FocusAreaEntity focusArea = focusAreaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + id));
        return mapTo(focusArea);
    }

    @Override
    @Transactional
    public FocusArea createFocusArea(FocusArea domain) {
        FocusAreaEntity focusArea = new FocusAreaEntity();
        focusArea.setName(domain.getName());
        focusArea.setDescription(domain.getDescription());

        FocusAreaEntity savedFocusArea = focusAreaRepository.save(focusArea);
        return mapTo(savedFocusArea);
    }

    @Override
    @Transactional
    public FocusArea updateFocusArea(Long id, FocusArea domain) {
        FocusAreaEntity focusArea = focusAreaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Focus Area not found with id: " + id));

        focusArea.setName(domain.getName());
        focusArea.setDescription(domain.getDescription());

        FocusAreaEntity updatedFocusArea = focusAreaRepository.save(focusArea);
        return mapTo(updatedFocusArea);
    }

    @Override
    @Transactional
    public void deleteFocusArea(Long id) {
        if (!focusAreaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Focus Area not found with id: " + id);
        }
        focusAreaRepository.deleteById(id);
    }

    private FocusArea mapTo(FocusAreaEntity focusArea) {
        FocusArea dto = new FocusArea();
        dto.setId(focusArea.getId());
        dto.setName(focusArea.getName());
        dto.setDescription(focusArea.getDescription());
        dto.setCreatedAt(focusArea.getCreatedAt());
        dto.setUpdatedAt(focusArea.getUpdatedAt());
        return dto;
    }
}
