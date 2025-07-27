package com.example.registrationProject.response;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Episode;
import com.example.registrationProject.entity.Language;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.EpisodeDto;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionResponse {
    private Long id;
    private String name;
    private String description;
    private String coverImage;
    private Status status;
    private LanguageDto languageId;
    private List<GenreDto> genreIds;
    private List<EpisodeDto> episodeIds;
    private List<ArtistDto> artistIds;

    private LocalDate releaseDate;
}
