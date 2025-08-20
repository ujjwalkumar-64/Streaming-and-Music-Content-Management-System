package com.example.registrationProject.response;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ArtistResponse {
    @JsonProperty("artist_id")
    private Long id;
    private String artistName;
    private String bio;
    private String profilePic;
    private UserDto user;
    private List<GenreDto> genres;
    private Status status;
}
