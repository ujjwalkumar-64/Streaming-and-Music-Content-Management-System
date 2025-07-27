package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.AlbumRepository;
import com.example.registrationProject.repository.ArtistRepository;
import com.example.registrationProject.repository.GenreRepository;
import com.example.registrationProject.repository.TrackRepository;
import com.example.registrationProject.request.AlbumRequest;
import com.example.registrationProject.response.AlbumResponse;
import com.example.registrationProject.response.DTO.*;
import com.example.registrationProject.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;


    @Override
    public AlbumResponse createAlbum(AlbumRequest albumRequest) {
        Album album = new Album();
        if(!albumRequest.getCoverImage().isEmpty()){
            try{
                 String profileUrl= userService.uploadPhoto(albumRequest.getCoverImage());
                 album.setCoverImage(profileUrl);
            }
            catch (Exception e){
                throw  new CustomException("Something went wrong while uploading the photo");
            }
        }

        List<Artist> artists = artistRepository.findAllById(albumRequest.getArtistIds());
        if(artists.size() != albumRequest.getArtistIds().size()){
            throw new CustomException("All Artist are not found some id are invalid");
        }
        album.setArtists(artists);

        if(!albumRequest.getTrackIds().isEmpty()){
            List<Track> tracks= trackRepository.findAllById(albumRequest.getTrackIds());
            if(tracks.size() != albumRequest.getTrackIds().size()){
                throw new CustomException("All Track are not found some id are invalid");
            }
            album.setTracks(tracks);
        }


         List<Genre> genres= genreRepository.findAllById(albumRequest.getGenreIds());
         if(genres.size() != albumRequest.getGenreIds().size()){
             throw new CustomException("All Genre are not found some id are invalid");
         }
         album.setAlbumGenres(genres);

        album.setReleaseDate(albumRequest.getReleaseDate());
        album.setTitle(albumRequest.getTitle());
        album.setDescription(albumRequest.getDescription());
        album.setStatus(Status.active);

        Album response = albumRepository.save(album);

        List<ArtistDto> artistDtos= new ArrayList<>();
        response.getArtists().forEach(artist->{
            artistDtos.add(new ArtistDto().builder()
                            .id(artist.getId())
                            .artistName(artist.getArtistName())

                    .build());

        });

        List<TrackDto> trackDtos= new ArrayList<>();
        response.getTracks().forEach(track->{
            trackDtos.add(new TrackDto().builder()
                    .id(track.getId())
                    .trackName(track.getTitle())
                    .trackUrl(track.getTrackRecord().getPath())
                    .build());
        });

        List<GenreDto> genreDtos= new ArrayList<>();
        response.getAlbumGenres().forEach(genre->{
            genreDtos.add(new GenreDto().builder()
                            .id(genre.getId())
                            .name(genre.getName())

                    .build());
        });



        return new AlbumResponse().builder()
                .id(response.getId())
                .coverImage(response.getCoverImage())
                .title(response.getTitle())
                .description(response.getDescription())
                .releaseDate(response.getReleaseDate())
                .artists(artistDtos)
                .tracks(trackDtos)
                .genres(genreDtos)
                .status(response.getStatus())
                .build();
    }

    @Override
    public List<AlbumDto> getAllAlbums(){
        List<Album> albums= albumRepository.findAll();
        List<AlbumDto> albumDtos= new ArrayList<>();
        albums.forEach(album->{
            albumDtos.add(new AlbumDto().builder()
                            .id(album.getId())
                            .title(album.getTitle())
                            .description(album.getDescription())
                            .releaseDate(album.getReleaseDate())

                    .build());
        });

        return albumDtos;

    }


}
