package com.example.registrationProject.service;

import com.example.registrationProject.entity.Track;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.TrendingDto;
import com.example.registrationProject.response.TrackResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackService {
    TrackResponse addTrack(TrackRequest trackRequest);
    TrackResponse updateTrack(TrackRequest trackRequest, Long id);
    TrackResponse getTrackById(Long id);
    TrackResponse getTrackByName(String name);
    TrackResponse getTrackByGenre(TrackRequest trackRequest);
    TrackResponse getTrackByLabel(TrackRequest trackRequest);
    List<TrackResponse> getAllTracks();

    void deleteTrackById(Long id);

    List<TrackResponse> getAllTracksForUser();

    void trackLike(Long trackId );
    void trackStreamLog(Long trackId, HttpServletRequest httpServletRequest );
    void trackUnlike(Long trackId );

    List<TrackResponse> getAllLikedTracksByUserId();

    Integer countLikedTracksByUserId(Long userId);

    Integer countUserLikedByTrackId(Long trackId);

    List<TrendingDto> getTrendingTracks();

}
