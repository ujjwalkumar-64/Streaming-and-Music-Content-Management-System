package com.example.registrationProject.request;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaylistRequest {
    private Long id;
    private String name;
    private String description;
    private List<Long> trackIds;
    private Long trackId;
    private Boolean isPublic;
}
