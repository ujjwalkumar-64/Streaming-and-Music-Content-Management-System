package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtistDto {
    @JsonProperty("artist_id")
    private Long id;
    @JsonProperty("artist_name")
    private String artistName;
    @JsonProperty("artist_bio")
    private String bio;
    @JsonProperty("profilePicUrl")
    private String profilePic;
    @JsonProperty("artist_status")
    private Status status;

    @JsonProperty("artist_genres")
    private List<GenreDto> genres;

    @JsonProperty("artist_joiningDate")
    private LocalDateTime joiningDate;


}
