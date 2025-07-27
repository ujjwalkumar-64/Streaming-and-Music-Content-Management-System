package com.example.registrationProject.response;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.EpisodeDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.example.registrationProject.response.DTO.SeasonDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PodcastResponse {
    private  Long id;
    private String title;
    private String description;
    private String coverImageUrl;

    private Status status;

    private Type type;

    private LanguageDto language;

    private List<SeasonDto> seasons;

    private List<EpisodeDto> episodes;

    private List<ArtistDto> artists;

    private LocalDate createdAt;
}
