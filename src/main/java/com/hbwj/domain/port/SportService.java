package com.hbwj.domain.port;

import com.hbwj.domain.model.Sport;

import java.util.List;

public interface SportService {
    List<Sport> getAllSports();
    Sport  getSportById(Long id);
    Sport  createSport(Sport  sport );
    Sport  updateSport(Long id, Sport  sport );
    void deleteSport(Long id);
}

