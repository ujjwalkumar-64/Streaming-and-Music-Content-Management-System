package com.example.registrationProject.service;

import com.example.registrationProject.request.SeasonRequest;
import com.example.registrationProject.response.SeasonResponse;
import org.springframework.stereotype.Service;

@Service
public interface SeasonService {
    SeasonResponse createSeason(SeasonRequest seasonRequest);
    SeasonResponse updateSeason(SeasonRequest seasonRequest);
    void deleteSeason(Long id);
}
