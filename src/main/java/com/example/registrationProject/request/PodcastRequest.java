package com.example.registrationProject.request;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Type;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.EpisodeDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.example.registrationProject.response.DTO.SeasonDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PodcastRequest {
    private  Long id;
    private String title;
    private String description;
    private MultipartFile coverImage;

    private Status status;

    private Type type;

    private Long languageId;

    private List<Long> seasonIds;

    private List<Long> episodeIds;

    private List<Long> artistIds;
}
