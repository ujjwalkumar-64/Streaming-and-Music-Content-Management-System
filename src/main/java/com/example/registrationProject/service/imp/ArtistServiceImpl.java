package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Artist;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.ArtistRepository;
import com.example.registrationProject.repository.GenreRepository;
import com.example.registrationProject.repository.UserRepository;
import com.example.registrationProject.request.ArtistRequest;
import com.example.registrationProject.response.ArtistResponse;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.service.ArtistService;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public ArtistResponse updateArtist(ArtistRequest artistRequest) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
                String profilePic=  userService.uploadPhoto(artistRequest.getProfilePic());
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
                .fullName(response.getUser().getFullName())
                .email(response.getUser().getEmail())
                .gender(response.getUser().getGender())
                .dob(response.getUser().getDob())
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




}
