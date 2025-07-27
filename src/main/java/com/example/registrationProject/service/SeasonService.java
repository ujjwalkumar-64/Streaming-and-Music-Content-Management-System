package com.example.registrationProject.service;

import com.example.registrationProject.request.SeasonRequest;
import com.example.registrationProject.response.SessionResponse;
import org.springframework.stereotype.Service;

@Service
public interface SeasonService {
    SessionResponse createSeason(SeasonRequest seasonRequest);
}
