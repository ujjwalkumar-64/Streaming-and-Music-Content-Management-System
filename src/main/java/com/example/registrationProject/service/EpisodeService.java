package com.example.registrationProject.service;

import com.example.registrationProject.request.EpisodeRequest;
import com.example.registrationProject.response.EpisodeResponse;
import org.springframework.stereotype.Service;

@Service
public interface EpisodeService {
    EpisodeResponse addEpisode(EpisodeRequest episodeRequest);
    EpisodeResponse updateEpisode(EpisodeRequest episodeRequest);
    void deleteEpisode(Long id);
}
