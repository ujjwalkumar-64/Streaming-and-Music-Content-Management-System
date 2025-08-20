package com.example.registrationProject.response;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.response.DTO.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrackResponse {
    @JsonProperty("track_id")
    private Long id;
    private String trackName;
    private String trackDescription;
    private String trackCoverImage;

    private String trackType;
    private String trackUrl;
    private Long trackDuration;

    private List<ArtistDto> artist;
    private AlbumDto album;
    private List<GenreDto> genre;
    private LabelDto label;
    private LanguageDto language;

}
