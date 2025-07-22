package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
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
public class LabelDto {
    @JsonProperty("label_id")
    private  Long id;
    @JsonProperty("label_name")
    private String name;
    @JsonProperty("label_description")
    private String description;
    @JsonProperty("label_logo")
    private String logo;


}
