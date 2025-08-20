package com.example.registrationProject.service;

import com.example.registrationProject.request.AlbumRequest;
import com.example.registrationProject.response.AlbumResponse;
import com.example.registrationProject.response.DTO.TrackDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlbumService {
    AlbumResponse createAlbum(AlbumRequest albumRequest);
    List<AlbumResponse> getAllAlbums();

    void deleteAlbum(Long id);
    AlbumResponse getAlbumById(Long id);

    List<TrackDto> getAllAlbumTracks(Long id);
    AlbumResponse updateAlbum(Long id,AlbumRequest albumRequest);

}
