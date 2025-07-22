package com.example.registrationProject.response;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.response.DTO.AlbumDto;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LabelResponse {
    @JsonProperty("label_id")
    private  Long id;
    private String name;

    private String description;

    private String logo;

    private UserDto ownerUser;

    private Status status;

    private List<TrackDto> tracks;
    private List<AlbumDto> albums;
}
