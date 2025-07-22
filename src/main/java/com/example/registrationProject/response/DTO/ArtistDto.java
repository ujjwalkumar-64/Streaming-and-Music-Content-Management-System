package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistDto {
    @JsonProperty("artist_id")
    private Long id;
    @JsonProperty("artist_name")
    private String artistName;
    @JsonProperty("artist_bio")
    private String bio;
    @JsonProperty("artist_profilePic")
    private String profilePic;

}
