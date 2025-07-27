package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.SeasonRequest;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.EpisodeDto;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.example.registrationProject.response.SessionResponse;
import com.example.registrationProject.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeasonServiceImpl implements SeasonService {
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private EpisodeServiceImpl episodeServiceImpl;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public SessionResponse createSeason(SeasonRequest seasonRequest) {
        try{
            Season season = new Season();
            season.setTitle(seasonRequest.getTitle());
            season.setDescription(seasonRequest.getDescription());
            if (seasonRequest.getCoverImage() != null) {
               String coverImg= episodeServiceImpl.uploadPhoto(seasonRequest.getCoverImage());
               season.setCoverImage(coverImg);
            }
            if(!seasonRequest.getArtistIds().isEmpty()){
                List<Artist> artists = artistRepository.findAllById(seasonRequest.getArtistIds());
                if(!artists.isEmpty()){
                    season.setArtists(artists);
                }

            }

            if( seasonRequest.getEpisodeIds() !=null && ! seasonRequest.getEpisodeIds().isEmpty()){
                List<Episode> episodes= episodeRepository.findAllById(seasonRequest.getEpisodeIds());
                if(!episodes.isEmpty()){
                    season.setEpisodes(episodes);
                }
            }

            if (seasonRequest.getGenreIds() !=null && ! seasonRequest.getGenreIds().isEmpty()){
                List<Genre> genres = genreRepository.findAllById(seasonRequest.getGenreIds());
                if(!genres.isEmpty()){
                    season.setSessionGenre(genres);
                }
            }

            if (seasonRequest.getLanguageId() !=null){
                Language language = languageRepository.findById(seasonRequest.getLanguageId()).orElseThrow(()->new CustomException("Language not found"));
                season.setLanguage(language);
            }

            Season response = seasonRepository.save(season);

            List<EpisodeDto> episodeDtos = new ArrayList<>();
            response.getEpisodes().forEach(episode -> {
                episodeDtos.add(new EpisodeDto().builder()
                        .id(episode.getId())
                        .title(episode.getTitle())
                        .description(episode.getDescription())
                        .episodeUrl(episode.getEpisodeRecord().getPath())
                        .build());
            });

            List<ArtistDto> artistDtos = new ArrayList<>();
            response.getArtists().forEach(artist -> {
                artistDtos.add(new ArtistDto().builder()
                                .id(artist.getId())
                                .artistName(artist.getArtistName())
                                .bio(artist.getBio())
                        .build());
            });

            List<GenreDto> genreDtos = new ArrayList<>();
            response.getSessionGenre().forEach(genre -> {
                genreDtos.add(new GenreDto().builder()
                                .id(genre.getId())
                                .name(genre.getName())
                        .build());
            });

            LanguageDto languageDto = new LanguageDto().builder()
                    .id(response.getLanguage().getId())
                    .language(response.getLanguage().getName())
                    .build();

            return SessionResponse.builder()
                    .id(response.getId())
                    .artistIds(artistDtos)
                    .episodeIds(episodeDtos)
                    .releaseDate(LocalDate.from(response.getReleaseDate()))
                    .status(response.getStatus())
                    .genreIds(genreDtos)
                    .languageId(languageDto)
                    .coverImage(response.getCoverImage())
                    .status(response.getStatus())
                    .description(response.getDescription())
                    .build();

        }
        catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }


}
