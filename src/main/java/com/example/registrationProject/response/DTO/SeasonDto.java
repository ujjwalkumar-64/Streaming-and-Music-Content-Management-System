package com.example.registrationProject.response.DTO;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SeasonDto {
    private Long id;
    private String title;
    private String description;
    private String coverImage;

    private List<EpisodeDto> episodes;

}
