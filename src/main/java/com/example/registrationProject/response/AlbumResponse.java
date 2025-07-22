package com.example.registrationProject.response;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.TrackDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AlbumResponse {
    @JsonProperty("album_id")
    private  Long id;

    private String title;
    private String description;

    private LocalDate releaseDate;

    private String coverImage;

    private List<ArtistDto> artists;

    private Status status;

    private List<TrackDto>tracks;

    private List<GenreDto> genres;

}
