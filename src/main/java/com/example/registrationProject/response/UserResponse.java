package com.example.registrationProject.response;

import com.example.registrationProject.entity.Gender;
import com.example.registrationProject.entity.Role;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    @JsonProperty("User_id")
    private Long id;
    private String fullName;
    private String email;
    private Status status;
    private Gender gender;
    private String imageUrl;
    private Role role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserResponse(Long id, String fullName, String email, Status status, Gender gender, LocalDate dob, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
        this.gender = gender;
        this.dob = dob;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
