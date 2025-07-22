package com.example.registrationProject.response.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreDto {
    @JsonProperty("genre_id")
    private Long id;
    @JsonProperty("genre_name")
    private String name;
}
