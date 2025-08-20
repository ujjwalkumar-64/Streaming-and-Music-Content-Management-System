package com.example.registrationProject.service.imp;


import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.PodcastRequest;
import com.example.registrationProject.response.DTO.EpisodeDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.example.registrationProject.response.DTO.SeasonDto;
import com.example.registrationProject.response.PodcastResponse;
import com.example.registrationProject.service.PodcastService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

        try {
            Podcast podcast = podcastRepository.findById(podcastRequest.getId()).orElseThrow(() -> new CustomException("Podcast Not Found"));

            if (podcastRequest.getCoverImage() != null && !podcastRequest.getCoverImage().isEmpty()) {
                String coverImage = episodeServiceImpl.uploadPhoto(podcastRequest.getCoverImage());
                podcast.setCoverImageUrl(coverImage);
            }

            if(podcastRequest.getTitle() != null && !podcastRequest.getTitle().isEmpty()) {
                podcast.setTitle(podcastRequest.getTitle());
            }
            if(podcastRequest.getDescription() != null && !podcastRequest.getDescription().isEmpty()) {
                podcast.setDescription(podcastRequest.getDescription());
            }

           if(podcastRequest.getLanguageId() !=null){
               Language language = languageRepository.findById(podcastRequest.getLanguageId()).orElse(null);
               podcast.setLanguage(language);
           }

           if(podcastRequest.getStatus()!=null){
               podcast.setStatus(podcast.getStatus());
           }

           if(podcastRequest.getType()!=null){
               podcast.setType(podcast.getType());
           }

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
    @Transactional
    public void deletePodcast(PodcastRequest podcastRequest) {
        try{
            Podcast podcast= podcastRepository.findById(podcastRequest.getId()).orElseThrow(() -> new CustomException("Podcast Not Found"));
            podcast.setStatus(Status.inactive);

            podcast.getSeasons().forEach(season -> {
                season.getEpisodes().forEach(episode -> {
                    episode.setStatus(Status.inactive);
                    episodeRepository.save(episode);
                });
                season.setStatus(Status.inactive);
                seasonRepository.save(season);

            });
            podcastRepository.save(podcast);
        }
        catch (Exception e) {
            throw  new CustomException(e.getMessage());
        }

    }

    @Override
    public List<PodcastResponse> getAllPodcasts(){

            List<PodcastResponse> podcastResponses = new ArrayList<>();
            List<Podcast> podcasts = podcastRepository.findAll();
            podcasts.stream().filter((podcast)->podcast.getStatus().equals(Status.active)).forEach(podcast -> {

                List<SeasonDto> seasonDtos = new ArrayList<>();

                podcast.getSeasons().stream().filter((season -> season.getStatus().equals(Status.active))).forEach(season -> {

                    List<EpisodeDto> episodeDtos = new ArrayList<>();

                    season.getEpisodes().stream().filter((episode -> episode.getStatus().equals(Status.active))).forEach(episode -> {

                        episodeDtos.add(new EpisodeDto().builder()
                                        .id(episode.getId())
                                        .description(episode.getDescription())
                                        .title(episode.getTitle())
                                        .duration(episode.getEpisodeRecord().getDuration())
                                        .episodeUrl(episode.getEpisodeRecord().getPath())
                                .build());

                    });

                    seasonDtos.add(new SeasonDto().builder()
                                    .id(season.getId())
                                    .coverImage(season.getCoverImage())
                                    .description(season.getDescription())
                                    .title(season.getTitle())
                                    .episodes(episodeDtos)
                            .build());


                });
                LanguageDto languageDto = new LanguageDto().builder()
                        .id(podcast.getLanguage().getId())
                        .language(podcast.getLanguage().getName())
                        .build();


                podcastResponses.add(new PodcastResponse().builder()
                                .id(podcast.getId())
                                .title(podcast.getTitle())
                                .description(podcast.getDescription())
                                .coverImageUrl(podcast.getCoverImageUrl())
                                .language(languageDto)
                                .seasons(seasonDtos)
                        .build());

            });

            return podcastResponses;

    }

    @Override
     public List<PodcastResponse> getAllUserBrowsingPodcasts(){
        List<Podcast> podcasts = podcastRepository.findAll();
        List<PodcastResponse> podcastResponses = new ArrayList<>();

        podcasts.forEach(podcast -> {
            List<SeasonDto> seasonDtos = new ArrayList<>();
            podcast.getSeasons().forEach(season -> {
                seasonDtos.add(new SeasonDto().builder()
                        .id(season.getId())
                        .title(season.getTitle())
                        .description(season.getDescription())
                        .coverImage(season.getCoverImage())
                        .build());

            });

            List<EpisodeDto> episodeDtos = new ArrayList<>();
            podcast.getEpisodes().forEach(episode -> {
                episodeDtos.add(new EpisodeDto().builder()
                        .id(episode.getId())
                        .episodeUrl(episode.getEpisodeRecord().getPath())
                        .duration(episode.getEpisodeRecord().getDuration())
                        .title(episode.getTitle())
                        .description(episode.getDescription())
                        .build());
            });

            podcastResponses.add(new PodcastResponse().builder()
                    .id(podcast.getId())
                    .title(podcast.getTitle())
                    .description(podcast.getDescription())
                    .coverImageUrl(podcast.getCoverImageUrl())
                    .status(podcast.getStatus())
                    .type(podcast.getType())
                    .seasons(seasonDtos)
                    .episodes(episodeDtos)
                    .build());
        });

        return podcastResponses;

    }


}
