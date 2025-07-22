package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Gender;
import com.example.registrationProject.entity.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonProperty("user_id")
    private Long id;
    @JsonProperty("user_fullName")
    private String fullName;
    @JsonProperty("user_email")
    private String email;
    @JsonProperty("user_gender")
    private Gender gender;
    @JsonProperty("user_profilePic")
    private String imageUrl;
    @JsonProperty("user_dateOfBirth")
    private LocalDate dob;
}
