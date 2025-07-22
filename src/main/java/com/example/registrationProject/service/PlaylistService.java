package com.example.registrationProject.service;

import com.example.registrationProject.request.PlaylistRequest;
import com.example.registrationProject.response.PlaylistResponse;
import org.springframework.stereotype.Service;

@Service
public interface PlaylistService {

    PlaylistResponse createPlaylist(PlaylistRequest playlistRequest);
    PlaylistResponse addTrackToPlaylist(PlaylistRequest playlistRequest);

}
