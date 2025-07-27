package com.example.registrationProject.service;

import com.example.registrationProject.request.EpisodeRequest;
import com.example.registrationProject.request.PodcastRequest;
import com.example.registrationProject.response.PodcastResponse;
import org.springframework.stereotype.Service;

@Service
public interface PodcastService {
    PodcastResponse createPodcast(PodcastRequest podcastRequest);
    PodcastResponse updatePodcast(PodcastRequest podcastRequest);
    void deletePodcast(PodcastRequest podcastRequest);
    PodcastResponse getAllPodcasts(EpisodeRequest episodeRequest);

}
