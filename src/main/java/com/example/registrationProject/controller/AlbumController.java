package com.example.registrationProject.controller;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.AlbumRequest;
import com.example.registrationProject.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @PostMapping(value = "album/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createAlbum(@ModelAttribute AlbumRequest albumRequest){
        try{
            return  ResponseEntity.ok(albumService.createAlbum(albumRequest));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "album/all")
    public ResponseEntity<Object> getAllAlbums(){
        try{
            return ResponseEntity.ok(albumService.getAllAlbums());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
