package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.ArtistRepository;
import com.example.registrationProject.repository.GenreRepository;
import com.example.registrationProject.request.ArtistRequest;
import com.example.registrationProject.response.ArtistResponse;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.service.ArtistService;
import com.example.registrationProject.service.CloudinaryService;
import com.example.registrationProject.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    RedisService redisService;

    @Override
    public ArtistResponse updateArtist(ArtistRequest artistRequest) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="artist:profile:email:"+user.getEmail();
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }

        String redisKey1="artist:admin:list";
        if(redisService.exists(redisKey1)){
            redisService.delete(redisKey1);
        }

        Artist artist = null;

        if(user.getRole().getRole().equals("ROLE_SUPER_ADMIN" )){
              artist = artistRepository.findById(artistRequest.getId()).orElseThrow(()->new CustomException("Invalid Artist Id"));
        }
        else if(user.getRole().getRole().equals("ROLE_ARTIST")){
              artist = artistRepository.findByUserId(user.getId()).orElseThrow(()->new CustomException("Invalid Artist Id or user role not  as artist"));
        }


        assert artist != null;
        if(artistRequest.getProfilePic() != null && !artistRequest.getProfilePic().isEmpty()){
            try{
                String profilePic=  cloudinaryService.uploadPhoto(artistRequest.getProfilePic());
                artist.setProfilePic(profilePic);
            }
            catch(Exception e){
                throw new CustomException("Invalid Profile Pic");
            }

        }

        List<Genre> genres = genreRepository.findAllById(artistRequest.getGenreIds());
        if(genres.size() != artistRequest.getGenreIds().size()){
            throw new CustomException("Invalid Genre Ids");
        }
        artist.setArtistGenres(genres);

        artist.setArtistName(artistRequest.getArtistName());
        artist.setBio(artistRequest.getBio());

        Artist response= artistRepository.save(artist);

        UserDto userDto= UserDto.builder()
                .id(response.getUser().getId())
                .build();

        List<GenreDto> genreDtos= new ArrayList<>();
        response.getArtistGenres().forEach(genre->{
            genreDtos.add(new GenreDto().builder()
                    .id(genre.getId())
                    .name(genre.getName())
                    .build());
        });

        return new ArtistResponse().builder()
                .id(response.getId())
                .artistName(response.getArtistName())
                .bio(response.getBio())
                .profilePic(response.getProfilePic())
                .status(response.getStatus())
                .user(userDto)
                .genres(genreDtos)
                .build();
    }

    @Override
    public ArtistDto getMyProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="artist:profile:email:"+user.getEmail();

        ArtistDto cachedResponse = (ArtistDto) redisService.get(redisKey);
        if(cachedResponse != null){
            return cachedResponse;
        }

        Artist artist = artistRepository.findByUserId(user.getId()).orElseThrow(()->new CustomException("Artist Profile Not Found"));
        List<Genre> genres = artist.getArtistGenres();
        List<GenreDto> genreDtos= new ArrayList<>();
        genres.forEach(genre->{
            genreDtos.add( GenreDto.builder()
                            .id(genre.getId())
                            .name(genre.getName())
                    .build());
        });
        ArtistDto result= ArtistDto.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .bio(artist.getBio())
                .profilePic(artist.getProfilePic())
                .status(artist.getStatus())
                .genres(genreDtos)
                .joiningDate(artist.getCreatedAt())
                .build();

        redisService.saveWithTTL(redisKey,result,10, TimeUnit.MINUTES);
        return result;
    }


    @Override
    public List<ArtistDto> getAllArtistProfile() throws CustomException{

        String redisKey="artist:admin:list";

        List<ArtistDto> cachedResponse = (List<ArtistDto>) redisService.get(redisKey);
        if(cachedResponse != null){
            return cachedResponse;
        }

        List<Artist> artists = artistRepository.findAll();

        List<ArtistDto> artistDtos= new ArrayList<>();
        if(artists.isEmpty()){
            throw new CustomException("Artists Profiles Not Found");
        }
        artists.forEach(artist->{
            List<Genre> genres = artist.getArtistGenres();
            List<GenreDto> genreDtos= new ArrayList<>();
            if(!genres.isEmpty()){
                genres.forEach(genre->{
                    genreDtos.add( GenreDto.builder()
                            .id(genre.getId())
                            .name(genre.getName())
                            .build());
                });

            }


            artistDtos.add(ArtistDto.builder()
                            .id(artist.getId())
                            .artistName(artist.getArtistName())
                            .bio(artist.getBio())
                            .profilePic(artist.getProfilePic())
                            .status(artist.getStatus())
                            .genres(genreDtos)
                            .joiningDate(artist.getCreatedAt())

                    .build());
        });

        redisService.saveWithTTL(redisKey,artistDtos,10, TimeUnit.MINUTES);

        return artistDtos;
    }



}
