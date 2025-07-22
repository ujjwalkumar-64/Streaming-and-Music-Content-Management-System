package com.example.registrationProject.service;

import com.example.registrationProject.request.ArtistRequest;
import com.example.registrationProject.response.ArtistResponse;
import org.springframework.stereotype.Service;

@Service
public interface ArtistService {
    ArtistResponse updateArtist(ArtistRequest artistRequest);
}
