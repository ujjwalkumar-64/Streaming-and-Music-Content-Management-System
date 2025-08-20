package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.service.TrackService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class TrackController {
    @Autowired
    private TrackService trackService;

    @PostMapping(value = "/track/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addTrack(@ModelAttribute  TrackRequest trackRequest) {
        try{
            return ResponseEntity.ok(trackService.addTrack(trackRequest));

        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "track/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateTrack(@ModelAttribute  TrackRequest trackRequest, @PathVariable Long id) {
        try{
            return ResponseEntity.ok(trackService.updateTrack(trackRequest,id));
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "track/search")
    public ResponseEntity<Object> getTrack(@RequestParam(required = false) Long id,
                                           @RequestParam(required = false) String name) {
        try {
            if (id != null) {
                return ResponseEntity.ok(trackService.getTrackById(id));
            } else if (name != null) {
                return ResponseEntity.ok(trackService.getTrackByName(name));
            } else {
                return ResponseEntity.badRequest().body("missing id or name to search");
            }
        } catch(CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "track/browsing")
    public ResponseEntity<Object> getTrackBrowsing(){
        try{
            return ResponseEntity.ok(trackService.getAllTracksForUser());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "track/all")
    public ResponseEntity<Object> getAllTracks(){
        try {
            return ResponseEntity.ok(trackService.getAllTracks());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Object> deleteTrackById(@PathVariable Long id) {
        try{
            trackService.deleteTrackById(id);
            return ResponseEntity.noContent().build();
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "track/{id}")
    public ResponseEntity<Object> getTrackById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(trackService.getTrackById(id));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "track/like/{trackId}")
    public ResponseEntity<Object> addTrackLike(@PathVariable Long trackId) {
        try{
             trackService.trackLike(trackId);
             return ResponseEntity.ok().body("Track like successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "track/unlike/{id}")
    public ResponseEntity<Object> removeTrackLike(@PathVariable Long id) {
        try{
            trackService.trackUnlike(id);
            return ResponseEntity.ok().body("Track unlike successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "track/stream/log/{id}")
    public ResponseEntity<Object> trackStreamLog(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        try{
            trackService.trackStreamLog(id,httpServletRequest);
            return ResponseEntity.ok().body("Track stream log successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "track/trending")
    public ResponseEntity<Object> getTrackTrending(){
        try{

            return ResponseEntity.ok(trackService.getTrendingTracks());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}


