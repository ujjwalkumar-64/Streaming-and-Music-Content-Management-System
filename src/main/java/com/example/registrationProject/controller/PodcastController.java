package com.example.registrationProject.controller;

import com.example.registrationProject.request.EpisodeRequest;
import com.example.registrationProject.request.PodcastRequest;
import com.example.registrationProject.request.SeasonRequest;
import com.example.registrationProject.response.PodcastResponse;
import com.example.registrationProject.service.EpisodeService;
import com.example.registrationProject.service.PodcastService;
import com.example.registrationProject.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PodcastController {
    @Autowired
    PodcastService podcastService;

    @Autowired
    SeasonService seasonService;

    @Autowired
    EpisodeService episodeService;

    @PostMapping(value = "/add/podcast",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addPodcast(@ModelAttribute PodcastRequest podcastRequest) {
        try{
            return ResponseEntity.ok(podcastService.createPodcast(podcastRequest));
        }
        catch(Exception e){
           return ResponseEntity.badRequest().body("Something went wrong while creating podcast" + e.getMessage());
        }
    }


    @PostMapping(value = "/add/season",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addSeason(@ModelAttribute SeasonRequest seasonRequest) {
        try{
            return ResponseEntity.ok(seasonService.createSeason(seasonRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while creating season" + e.getMessage());
        }
    }

    @PostMapping(value = "update/season",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateSeason(@ModelAttribute SeasonRequest seasonRequest) {
        try{
            return ResponseEntity.ok(seasonService.updateSeason(seasonRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while updating season" + e.getMessage());
        }
    }

    @PostMapping(value = "/add/episode",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addEpisode(@ModelAttribute EpisodeRequest episodeRequest) {
        try{
            return ResponseEntity.ok(episodeService.addEpisode(episodeRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while creating episode" + e.getMessage());
        }
    }

    @PostMapping(value = "update/podcast",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updatePodcast(@ModelAttribute PodcastRequest podcastRequest) {
        try{
            return ResponseEntity.ok(podcastService.updatePodcast(podcastRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while updating podcast" + e.getMessage());
        }
    }

    @DeleteMapping(value="/delete/podcast")
    public ResponseEntity<Object> deletePodcast(PodcastRequest podcastRequest) {
        try{
            podcastService.deletePodcast(podcastRequest);
            return ResponseEntity.ok().body("Podcast has been deleted");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while deleting podcast" + e.getMessage());
        }
    }

    @GetMapping(value = "get/all/podcasts")
    public ResponseEntity<Object> getAllPodcasts() {
        try{
            return ResponseEntity.ok(podcastService.getAllPodcasts());
        }
        catch(Exception e){
            return  ResponseEntity.badRequest().body("Something went wrong while getting all podcasts");
        }
    }

    @GetMapping(value = "get/browsing/podcasts")
    public ResponseEntity<Object> getAllBrowsingPodcasts() {
        try{
            return ResponseEntity.ok(podcastService.getAllUserBrowsingPodcasts());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong while getting all podcasts");
        }
    }




}
