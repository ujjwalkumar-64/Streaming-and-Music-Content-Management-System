package com.example.registrationProject.service;

import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.response.TrackResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackService {
    TrackResponse addTrack(TrackRequest trackRequest);
    TrackResponse updateTrack(TrackRequest trackRequest);
    TrackResponse getTrackById(Long id);
    TrackResponse getTrackByName(String name);
    TrackResponse getTrackByGenre(TrackRequest trackRequest);
    TrackResponse getTrackByLabel(TrackRequest trackRequest);
    List<TrackResponse> getAllTracks();

    void deleteTrackById(TrackRequest trackRequest);


}
