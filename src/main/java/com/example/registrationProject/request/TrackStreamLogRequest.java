package com.example.registrationProject.request;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackStreamLogRequest {
    private Long id;

    private Long userId;

    private Long trackId;

    private String deviceIp;
    private String userAgent;

    private LocalDateTime playedAt;
    private Status status;


}
