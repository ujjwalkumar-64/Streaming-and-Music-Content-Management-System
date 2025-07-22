package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.service.TrackService;
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

    @PostMapping(value = "track/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateTrack(@ModelAttribute  TrackRequest trackRequest) {
        try{
            return ResponseEntity.ok(trackService.updateTrack(trackRequest));
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



    @GetMapping(value = "track/all")
    public ResponseEntity<Object> getAllTracks(){
        try {
            return ResponseEntity.ok(trackService.getAllTracks());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "track/deleteById")
    public ResponseEntity<Object> deleteTrackById(@RequestBody TrackRequest trackRequest) {
        try{
            trackService.deleteTrackById(trackRequest);
            return ResponseEntity.noContent().build();
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "track/deleteByName")
    public ResponseEntity<Object> deleteTrackByName(@RequestBody TrackRequest trackRequest) {
        try{
            trackService.deleteTrackByName(trackRequest);
            return ResponseEntity.noContent().build();
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}


