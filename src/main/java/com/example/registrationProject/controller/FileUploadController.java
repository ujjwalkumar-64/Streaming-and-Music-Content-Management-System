package com.example.registrationProject.controller;

import com.example.registrationProject.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping()
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/{type}")
    public ResponseEntity<Object> uploadFile(@ModelAttribute MultipartFile file, @PathVariable String type) {
        try {
            String resourceType = type.equalsIgnoreCase("audio") ? "video" : "image";
            Map result = cloudinaryService.uploadFile(file, resourceType);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
