package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackDto {
    @JsonProperty("track_id")
    private Long id;
    @JsonProperty("track_name")
    private String trackName;
    @JsonProperty("track_description")
    private String trackDescription;

    @JsonProperty("track_url")
    private String trackUrl;
    @JsonProperty("track_duration")
    private Long trackDuration;

}
