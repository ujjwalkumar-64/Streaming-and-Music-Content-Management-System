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
import com.example.registrationProject.service.CloudinaryService;
import com.example.registrationProject.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RedisService redisService;



    @Override
    public AlbumResponse createAlbum(AlbumRequest albumRequest) {

        String redisKey="albums:list:all";
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }

        Album album = new Album();
        if(!albumRequest.getCoverImage().isEmpty()){
            try{
                 String profileUrl= cloudinaryService.uploadPhoto(albumRequest.getCoverImage());
                 album.setCoverImage(profileUrl);
            }
            catch (Exception e){
                throw  new CustomException("Something went wrong while uploading the photo");
            }
        }

        if(albumRequest.getArtistIds()!=null && !albumRequest.getArtistIds().isEmpty()){
            List<Artist> artists = artistRepository.findAllById(albumRequest.getArtistIds());
            if(artists.size() != albumRequest.getArtistIds().size()){
                throw new CustomException("All Artist are not found some id are invalid");
            }
            album.setArtists(artists);
        }


        if(albumRequest.getTrackIds() != null && !albumRequest.getTrackIds().isEmpty()) {

                List<Track> tracks= trackRepository.findAllById(albumRequest.getTrackIds());
                if(tracks.size() != albumRequest.getTrackIds().size()){
                    throw new CustomException("All Track are not found some id are invalid");
                }
                album.setTracks(tracks);

        }


        if(albumRequest.getGenreIds() != null && !albumRequest.getGenreIds().isEmpty()) {
            List<Genre> genres= genreRepository.findAllById(albumRequest.getGenreIds());
            if(genres.size() != albumRequest.getGenreIds().size()){
                throw new CustomException("All Genre are not found some id are invalid");
            }
            album.setAlbumGenres(genres);
        }


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

        if(response.getTracks() !=null && !response.getTracks().isEmpty()){
            response.getTracks().forEach(track->{
                trackDtos.add(new TrackDto().builder()
                        .id(track.getId())
                        .trackName(track.getTitle())
                        .trackUrl(track.getTrackRecord().getPath())
                        .build());
            });

        }

        List<GenreDto> genreDtos= new ArrayList<>();
        if(response.getAlbumGenres()!=null && !response.getAlbumGenres().isEmpty()){
            response.getAlbumGenres().forEach(genre->{
                genreDtos.add(new GenreDto().builder()
                        .id(genre.getId())
                        .name(genre.getName())

                        .build());
            });
        }




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
    public List<AlbumResponse> getAllAlbums(){

        String redisKey= "albums:list:all";

        List<AlbumResponse> cachedResponse= (List<AlbumResponse>) redisService.get(redisKey);

        if(cachedResponse!=null){
            return cachedResponse;
        }

        List<Album> albums= albumRepository.findAll();
        List<AlbumResponse> response= new ArrayList<>();

            albums.forEach(album->{
                List<ArtistDto> artistDtos= new ArrayList<>();

                if(album.getArtists()!=null && !album.getArtists().isEmpty()){
                    album.getArtists().forEach(artist->{
                        artistDtos.add(new ArtistDto().builder()
                                        .id(artist.getId())
                                        .artistName(artist.getArtistName())
                                .build());
                    }) ;}

                    List<GenreDto> genreDtos= new ArrayList<>();
                    if(album.getAlbumGenres()!=null && !album.getAlbumGenres().isEmpty()){
                        album.getAlbumGenres().forEach(genre->{
                            genreDtos.add(new GenreDto().builder()
                                            .id(genre.getId())
                                            .name(genre.getName())
                                    .build());
                        });
                    }

                    List<TrackDto> trackDtos= new ArrayList<>();
                    if(album.getTracks()!=null && !album.getTracks().isEmpty()){
                        album.getTracks().forEach(track->{
                            List<ArtistDto> trackArtist= new ArrayList<>();

                            track.getArtists().forEach(artist->{
                                trackArtist.add(new ArtistDto().builder()
                                                .id(artist.getId())
                                                .artistName(artist.getArtistName())
                                        .build()

                                );
                            });

                            trackDtos.add(new TrackDto().builder()
                                            .id(track.getId())
                                            .trackName(track.getTitle())
                                            .trackDuration(track.getTrackRecord().getDuration())
                                            .trackUrl(track.getTrackRecord().getPath())
                                            .coverImage(track.getCoverImage())
                                            .artists(trackArtist)


                                    .build());
                        });
                    }


                response.add(new AlbumResponse().builder()
                                .id(album.getId())
                                .tracks(trackDtos)
                                .releaseDate(album.getReleaseDate())
                                .title(album.getTitle())
                                .description(album.getDescription())
                                .artists(artistDtos)
                                .genres(genreDtos)
                                .coverImage(album.getCoverImage())
                                .status(album.getStatus())
                        .build());

            });

            redisService.saveWithTTL(redisKey,response,10, TimeUnit.MINUTES);

            return response;



    }



    @Override
    public  void deleteAlbum(Long id){
        Album album = albumRepository.findById(id).orElseThrow(()->new CustomException("Album not found"));
        album.setStatus(Status.inactive);
        albumRepository.save(album);
    };

    @Override
    public AlbumResponse getAlbumById(Long id){
        Album album = albumRepository.findById(id).orElseThrow(()->new CustomException("Album not found"));
        List<ArtistDto> artistDtos= new ArrayList<>();
        List<GenreDto> genreDtos= new ArrayList<>();
        List<TrackDto> trackDtos= new ArrayList<>();

        album.getArtists().forEach(artist->{
            artistDtos.add(new ArtistDto().builder()
                            .id(artist.getId())
                            .artistName(artist.getArtistName())
                    .build());
        });

        album.getAlbumGenres().forEach(genre->{
            genreDtos.add(new GenreDto().builder()
                            .id(genre.getId())
                    .name(genre.getName())
                    .build());
        });

        album.getTracks().forEach(track->{
            trackDtos.add(new TrackDto().builder()
                    .id(track.getId())
                    .trackName(track.getTitle())
                    .trackUrl(track.getTrackRecord().getPath())
                    .trackDuration(track.getTrackRecord().getDuration())
                    .trackDescription(track.getDescription())
                    .build());
        });


       return AlbumResponse.builder()
                .id(album.getId())
                .coverImage(album.getCoverImage())
                .title(album.getTitle())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .artists(artistDtos)
                .tracks(trackDtos)
                .genres(genreDtos)
                .status(album.getStatus())
                .build();

    }

    @Override

    public List<TrackDto> getAllAlbumTracks(Long id){

        String redisKey="album:list:track:id:"+id;

        List<TrackDto> cachedResponse= (List<TrackDto>) redisService.get(redisKey);
        if(cachedResponse!=null){
            return cachedResponse;
        }

        Album album = albumRepository.findById(id).orElseThrow(()->new CustomException("Album not found"));
        List<TrackDto> trackDtos= new ArrayList<>();
        album.getTracks().forEach(track->{
            List<ArtistDto> artistDtos= new ArrayList<>();
            if(track.getArtists()!=null && !track.getArtists().isEmpty()){
                 track.getArtists().forEach(artist->{
                     artistDtos.add(new ArtistDto().builder()
                                     .id(artist.getId())
                                     .artistName(artist.getArtistName())
                             .build());
                 });
            }
            List<GenreDto> genreDtos= new ArrayList<>();
            if(track.getGenres()!=null && !track.getGenres().isEmpty()){
                track.getGenres().forEach(genre->{
                    genreDtos.add(new GenreDto().builder()
                                    .id(genre.getId())
                                    .name(genre.getName())
                            .build());
                });
            }
            trackDtos.add(new TrackDto().builder()
                    .id(track.getId())
                    .trackName(track.getTitle())
                    .trackUrl(track.getTrackRecord().getPath())
                    .trackDuration(track.getTrackRecord().getDuration())
                    .trackDescription(track.getDescription())
                            .artists(artistDtos)
                            .genres(genreDtos)
                            .coverImage(track.getCoverImage())
                    .build());
        });

        redisService.saveWithTTL(redisKey,trackDtos,10, TimeUnit.MINUTES);

        return trackDtos;
    }

    @Override
    public AlbumResponse updateAlbum(Long id,AlbumRequest albumRequest) {

        String redisKey="albums:list:all";
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }


        try {
            Album album = albumRepository.findById(id).orElseThrow(() -> new CustomException("Album not found"));
            if (albumRequest.getTitle() != null) {
                album.setTitle(albumRequest.getTitle());
            }
            if (albumRequest.getDescription() != null) {
                album.setDescription(albumRequest.getDescription());
            }
            if (albumRequest.getReleaseDate() != null) {
                album.setReleaseDate(albumRequest.getReleaseDate());
            }
            if (albumRequest.getCoverImage() != null) {
                String coverImage = cloudinaryService.uploadPhoto(albumRequest.getCoverImage());
                album.setCoverImage(coverImage);
            }

            if(albumRequest.getArtistIds()!=null && !albumRequest.getArtistIds().isEmpty()){
                List<Artist> artists = artistRepository.findAllById(albumRequest.getArtistIds());
                if(artists.size() != albumRequest.getArtistIds().size()){
                    throw new CustomException("All Artist are not found some id are invalid");
                }
                album.setArtists(artists);
            }


            if(albumRequest.getTrackIds() != null && !albumRequest.getTrackIds().isEmpty()) {

                List<Track> tracks= trackRepository.findAllById(albumRequest.getTrackIds());
                if(tracks.size() != albumRequest.getTrackIds().size()){
                    throw new CustomException("All Track are not found some id are invalid");
                }
                album.setTracks(tracks);

            }
            if(albumRequest.getGenreIds() != null && !albumRequest.getGenreIds().isEmpty()) {
                List<Genre> genres= genreRepository.findAllById(albumRequest.getGenreIds());
                if(genres.size() != albumRequest.getGenreIds().size()){
                    throw new CustomException("All Genre are not found some id are invalid");
                }
                album.setAlbumGenres(genres);
            }

            Album response = albumRepository.save(album);

            List<ArtistDto> artistDtos= new ArrayList<>();
            response.getArtists().forEach(artist->{
                artistDtos.add(new ArtistDto().builder()
                        .id(artist.getId())
                        .artistName(artist.getArtistName())

                        .build());

            });

            List<TrackDto> trackDtos= new ArrayList<>();

            if(response.getTracks() !=null && !response.getTracks().isEmpty()){
                response.getTracks().forEach(track->{
                    trackDtos.add(new TrackDto().builder()
                            .id(track.getId())
                            .trackName(track.getTitle())
                            .trackUrl(track.getTrackRecord().getPath())
                            .build());
                });

            }

            List<GenreDto> genreDtos= new ArrayList<>();
            if(response.getAlbumGenres()!=null && !response.getAlbumGenres().isEmpty()){
                response.getAlbumGenres().forEach(genre->{
                    genreDtos.add(new GenreDto().builder()
                            .id(genre.getId())
                            .name(genre.getName())

                            .build());
                });
            }




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
        catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
