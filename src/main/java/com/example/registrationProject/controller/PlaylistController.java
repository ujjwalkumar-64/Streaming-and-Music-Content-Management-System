package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.PlaylistRequest;
import com.example.registrationProject.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @PostMapping(value = "/playlist/create")
    public ResponseEntity<Object> createPlaylist(@RequestBody PlaylistRequest playlistRequest) {
        try{
           return ResponseEntity.ok(playlistService.createPlaylist(playlistRequest));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "playlist/addTrackToPlaylist")
    public ResponseEntity<Object> addTrackToPlaylist(@RequestBody PlaylistRequest playlistRequest) {
        try{
            return ResponseEntity.ok(playlistService.addTrackToPlaylist(playlistRequest));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "playlist/fetchMyPlaylists")
    public ResponseEntity<Object> getMyPlaylists() {
        try{
            return ResponseEntity.ok(playlistService.getUserPlaylist());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/playlist/getAllPlaylists")
    public ResponseEntity<Object> getAllPlaylists() {
        try{
            return ResponseEntity.ok(playlistService.getAllPlaylists());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
