package com.example.registrationProject.response.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrendingDto {
    TrackDto track;
    Integer streamCount;
    Integer trackLikedCount;
    Integer trendingCount;
}
