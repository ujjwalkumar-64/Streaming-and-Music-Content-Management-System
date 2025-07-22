package com.example.registrationProject.service;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.request.AlbumRequest;
import com.example.registrationProject.response.AlbumResponse;
import com.example.registrationProject.response.DTO.AlbumDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlbumService {
    AlbumResponse createAlbum(AlbumRequest albumRequest);
    List<AlbumDto> getAllAlbums();
}
