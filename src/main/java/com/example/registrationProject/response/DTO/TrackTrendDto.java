package com.example.registrationProject.response.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackTrendDto {
    private Long trackId;
    private Integer countLike;
    private Integer countStream;
    private Integer calcTrend;


}

