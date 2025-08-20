package com.example.registrationProject.response.DTO;

import com.example.registrationProject.entity.Gender;
import com.example.registrationProject.entity.Language;
import com.example.registrationProject.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String fullName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("profilePic")
    private String imageUrl;
    @JsonProperty("role")
    private String userRole;
    @JsonProperty("dob")
    private LocalDate dob;
    @JsonProperty("status")
    private Status status;

    @JsonProperty("language")
    private List<LanguageDto> languages;

    @JsonProperty("joinDate")
    private LocalDateTime joiningDate;
}
