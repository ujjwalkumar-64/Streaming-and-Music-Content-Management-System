package com.example.registrationProject.request;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumRequest {


    private String title;
    private String description;

    private LocalDate releaseDate;

    private MultipartFile coverImage;

    private List<Long> artistIds;

    private Status status;


    private List<Long>trackIds;

    private List<Long> genreIds;

}
