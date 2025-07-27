package com.example.registrationProject.service;

import com.example.registrationProject.request.ArtistRequest;
import com.example.registrationProject.response.ArtistResponse;
import com.example.registrationProject.response.DTO.ArtistDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtistService {
    ArtistResponse updateArtist(ArtistRequest artistRequest);
    ArtistDto getMyProfile();
    List<ArtistDto> getAllArtistProfile();

}
