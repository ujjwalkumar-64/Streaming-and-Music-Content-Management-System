package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.LabelRequest;
import com.example.registrationProject.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping(value = "/label/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateLabel(@ModelAttribute LabelRequest labelRequest) {
        try{
            return ResponseEntity.ok().body(labelService.updateLabel(labelRequest));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/label/profile")
    public ResponseEntity<Object> getProfile(){
        try{
            return ResponseEntity.ok().body(labelService.getMyProfile());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/labels")
    public ResponseEntity<Object> getLabels(){
        try{
            return  ResponseEntity.ok().body(labelService.getAllLabels());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
