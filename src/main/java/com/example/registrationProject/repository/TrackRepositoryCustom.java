package com.example.registrationProject.repository;

import com.example.registrationProject.response.DTO.TrackTrendDto;

import java.util.List;


public interface TrackRepositoryCustom {
    List<TrackTrendDto> findAllTrendingTrackIds();
}
