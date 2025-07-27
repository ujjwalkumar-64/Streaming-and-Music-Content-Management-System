package com.example.registrationProject.request;

import com.example.registrationProject.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeasonRequest {
    private Long id;

    @Column(unique = true)
    private String title;
    private String description;
    private MultipartFile coverImage;
    private Status status;
    private Long languageId;
    private List<Long> genreIds;
    private List<Long> episodeIds;
    private List<Long> artistIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

}
