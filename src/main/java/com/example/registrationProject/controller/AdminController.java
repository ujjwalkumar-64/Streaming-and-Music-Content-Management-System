package com.example.registrationProject.controller;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.repository.ArtistRepository;
import com.example.registrationProject.repository.GenreRepository;
import com.example.registrationProject.repository.LanguageRepository;
import com.example.registrationProject.service.AdminService;
import com.example.registrationProject.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping

public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    TrackService trackService;

    @GetMapping(value = "/genres")
    public ResponseEntity<Object> getGenres(){
        try{
        return ResponseEntity.ok(adminService.getGenres());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/languages")
    public ResponseEntity<Object> getLanguages(){
        try{
        return ResponseEntity.ok(adminService.getLanguages());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/count/track/like/{trackId}")
    public ResponseEntity<Object> getCountTrackLike(@PathVariable Long trackId){
        try{
            return ResponseEntity.ok(trackService.countUserLikedByTrackId(trackId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
