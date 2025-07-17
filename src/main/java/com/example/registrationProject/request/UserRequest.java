package com.example.registrationProject.request;

import com.example.registrationProject.entity.Gender;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserRequest {
    private String fullName;
    private String email;
    private String password;
    private String newPassword;


     @Enumerated(EnumType.STRING)
    private Gender gender;
    private MultipartFile file;
    private String otp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    private String role;


    @Enumerated(EnumType.STRING)
    private Status status=Status.inactive;


}
