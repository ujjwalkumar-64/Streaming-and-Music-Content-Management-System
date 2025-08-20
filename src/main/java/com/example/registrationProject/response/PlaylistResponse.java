package com.example.registrationProject.response;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.Type;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistResponse {
    @JsonProperty("playlist_id")
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Type type;
    private UserDto creator;
    private List<TrackDto> tracks;
}
