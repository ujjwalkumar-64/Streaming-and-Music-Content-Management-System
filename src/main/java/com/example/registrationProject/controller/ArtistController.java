package com.example.registrationProject.controller;

import com.example.registrationProject.request.ArtistRequest;
import com.example.registrationProject.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping(value = "artist/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateArtist(@ModelAttribute ArtistRequest artistRequest) {
        try{
            return ResponseEntity.ok().body(artistService.updateArtist(artistRequest));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "artist/profile")
    public ResponseEntity<Object> getArtistProfile(){
        try{
            return ResponseEntity.ok(artistService.getMyProfile());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "artist/all")
    public ResponseEntity<Object> getAllArtistsProfile(){
        try {
            return ResponseEntity.ok(artistService.getAllArtistProfile());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
