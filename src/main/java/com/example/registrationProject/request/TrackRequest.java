package com.example.registrationProject.request;

import com.example.registrationProject.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackRequest {
    private Long id;
    private String title;
    private String description;
    private MultipartFile coverImage;
    private String duration;
    private Status status;
    private MultipartFile file;

    private Long albumId;
    private Long labelId;
    private List<Long> artistIds;
    private List<Long> genreIds;

}
