package com.example.registrationProject.response.DTO;

import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SeasonDto {
    private Long id;
    private String title;
    private String description;

}
