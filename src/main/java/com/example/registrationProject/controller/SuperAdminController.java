package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.service.ArtistService;
import com.example.registrationProject.service.TrackService;
import com.example.registrationProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SuperAdminController {
    @Autowired
    UserService userService;
    @Autowired
    ArtistService artistService;
    @Autowired
    TrackService trackService;


    @GetMapping("/admin/totalUsers/count")
    public ResponseEntity<Object> getTotalUsersCount(){
        try{
            return ResponseEntity.ok(userService.countAllUsers());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/getAllUsers")
    public ResponseEntity<Object> getAllUsers(){
        try{
            return ResponseEntity.ok(userService.getAllUsers());
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
