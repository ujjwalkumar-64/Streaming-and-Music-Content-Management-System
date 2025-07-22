package com.example.registrationProject.service;

import com.example.registrationProject.entity.Track;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.response.DTO.TrackDto;
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
    List<TrackDto> getAllTracks();

    void deleteTrackById(TrackRequest trackRequest);
    void deleteTrackByName(TrackRequest trackRequest);

}
