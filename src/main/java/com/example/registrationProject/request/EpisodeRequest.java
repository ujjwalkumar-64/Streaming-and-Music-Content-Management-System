package com.example.registrationProject.request;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.EpisodeRecord;
import com.example.registrationProject.entity.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EpisodeRequest {
    private Long id;
    private String title;
    private String description;
    private Long languageId;
    private MultipartFile coverImage;

    private Boolean isPublic;

    private Status status;

    private MultipartFile file;

    private List<Long> artistIds;
}
