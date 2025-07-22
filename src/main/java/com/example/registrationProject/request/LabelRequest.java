package com.example.registrationProject.request;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabelRequest {
    private Long id;
    private String name;

    private String description;

    private String logo;

    private Long ownerId;

    private Status status;


    private List<Long> trackIds;

    private List<Long> albumIds;
}
