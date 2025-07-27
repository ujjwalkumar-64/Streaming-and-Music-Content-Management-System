package com.example.registrationProject.response;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.EpisodeRecord;
import com.example.registrationProject.entity.Language;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.response.DTO.ArtistDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EpisodeResponse {

    private Long id;
    private String title;
    private String description;
    private String coverImage;


    @Enumerated(EnumType.STRING)
    private Status status;

    private String language;

    private String episodeUrl;
    private String type;
    private Long duration;

    private List<ArtistDto> artists;

    private LocalDateTime createdAt;

}
