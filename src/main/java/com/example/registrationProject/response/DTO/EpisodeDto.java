package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EpisodeDto {
    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private Status status;
    private String language;
    private String episodeUrl;
    private String type;
    private Long duration;

    List<ArtistDto> artists;


}
