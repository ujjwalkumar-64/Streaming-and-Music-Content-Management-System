package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
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
public class LabelDto {
    @JsonProperty("label_id")
    private  Long id;
    @JsonProperty("label_name")
    private String name;
    @JsonProperty("label_description")
    private String description;
    @JsonProperty("logoUrl")
    private String logo;
    @JsonProperty("label_status")
    private Status status;
    @JsonProperty("label_ownerId")
    private Long ownerId;
    @JsonProperty("label_joiningDate")
    private LocalDateTime joiningDate;
    @JsonProperty("label_ownerName")
    private String ownerName;


}
