package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDto {
    @JsonProperty("album_id")
    private  Long id;
    @JsonProperty("album_name")
    private String title;
    @JsonProperty("album_description")
    private String description;
    @JsonProperty("album_releaseDate")
    private LocalDate releaseDate;

}
