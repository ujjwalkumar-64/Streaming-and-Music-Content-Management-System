package com.example.registrationProject.service.imp;


import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.EpisodeRequest;
import com.example.registrationProject.request.PodcastRequest;
import com.example.registrationProject.response.PodcastResponse;
import com.example.registrationProject.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class PodcastServiceImpl implements PodcastService {

    @Autowired
    PodcastRepository podcastRepository;

    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EpisodeServiceImpl episodeServiceImpl;

    @Override
    public PodcastResponse createPodcast(PodcastRequest podcastRequest) {

        try {
            Podcast podcast = new Podcast();

            if (podcastRequest.getCoverImage() != null && !podcastRequest.getCoverImage().isEmpty()) {
                String coverImage = episodeServiceImpl.uploadPhoto(podcastRequest.getCoverImage());
                podcast.setCoverImageUrl(coverImage);
            }

            podcast.setTitle(podcastRequest.getTitle());
            podcast.setDescription(podcastRequest.getDescription());
            Language language = languageRepository.findById(podcastRequest.getLanguageId()).orElse(null);
            podcast.setLanguage(language);

            podcast.setStatus(Status.active);
            podcast.setType(Type.Public);

            if (podcastRequest.getArtistIds() != null && !podcastRequest.getArtistIds().isEmpty()) {
                List<Artist> artists = artistRepository.findAllById(podcastRequest.getArtistIds());
                if (!artists.isEmpty()) {
                    podcast.setArtists(artists);
                }

            }

            if (podcastRequest.getSeasonIds() != null && !podcastRequest.getSeasonIds().isEmpty()) {
                List<Season> seasons = seasonRepository.findAllById(podcastRequest.getSeasonIds());
                if (!seasons.isEmpty()) {
                    podcast.setSeasons(seasons);
                }
            }

            if (podcastRequest.getEpisodeIds() != null && !podcastRequest.getEpisodeIds().isEmpty()) {
                List<Episode> episodes = episodeRepository.findAllById(podcastRequest.getEpisodeIds());
                if (!episodes.isEmpty()) {
                    podcast.setEpisodes(episodes);
                }
            }

            return new PodcastResponse().builder()
                    .id(podcast.getId())
                    .title(podcast.getTitle())
                    .description(podcast.getDescription())
                    .coverImageUrl(podcast.getCoverImageUrl())
                    .status(podcast.getStatus())
                    .type(podcast.getType())

                    .createdAt(LocalDate.from(podcast.getCreatedAt()))
                    .build();

        }
        catch (Exception e) {
            throw  new CustomException(e.getMessage());
        }
}

    @Override
    public PodcastResponse updatePodcast(PodcastRequest podcastRequest) {
        return  new PodcastResponse();
    }

    @Override
    public void deletePodcast(PodcastRequest podcastRequest) {

    }

    @Override
    public PodcastResponse getAllPodcasts(EpisodeRequest episodeRequest){
        return  new PodcastResponse();
    }


}
