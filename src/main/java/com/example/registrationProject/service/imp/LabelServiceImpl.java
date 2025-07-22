package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Label;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.AlbumRepository;
import com.example.registrationProject.repository.LabelRepository;
import com.example.registrationProject.repository.TrackRepository;
import com.example.registrationProject.request.LabelRequest;
import com.example.registrationProject.response.DTO.AlbumDto;
import com.example.registrationProject.response.DTO.LabelDto;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.LabelResponse;
import com.example.registrationProject.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
     private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    // validation require label own update and also logo
    @Override
    public LabelResponse updateLabel(LabelRequest labelRequest) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Label label= null;

         if(user.getRole().getRole().equals("ROLE_SUPER_ADMIN")){
            label= labelRepository.findById(labelRequest.getId()).orElseThrow(()-> new CustomException("label not found or invalid id"));
         }
          else if(user.getRole().getRole().equals("ROLE_LABEL")){
              label= labelRepository.findByUserId(user.getId()).orElseThrow(()-> new CustomException("label not found or invalid user"));
         }
          assert label != null;

          if(labelRequest.getName() !=null){
              label.setName(labelRequest.getName());
          }

          if(labelRequest.getDescription() !=null){
              label.setDescription(labelRequest.getDescription());
          }

          if(labelRequest.getAlbumIds() !=null){
              List<Album> album= albumRepository.findAllById(labelRequest.getAlbumIds());
              if(album.size() != labelRequest.getAlbumIds().size()){
                  throw new CustomException("Invalid Album Ids or some album not found");
              }
              label.setAlbums(album);
          }

           if(labelRequest.getTrackIds() !=null){
               List<Track> track= trackRepository.findAllById(labelRequest.getTrackIds());
               if(track.size() != labelRequest.getTrackIds().size()){
                   throw new CustomException("Invalid Track Ids or some track not found");
               }
               label.setTracks(track);
           }

          Label response= labelRepository.save(label);

           List<AlbumDto> albumDtos= new ArrayList<>();
           response.getAlbums().stream().forEach(album->{
               albumDtos.add(new AlbumDto().builder()
                               .id(album.getId())
                               .title(album.getTitle())
                               .releaseDate(album.getReleaseDate())
                               .description(album.getDescription())
                       .build());

           });

           List<TrackDto> trackDtos= new ArrayList<>();
           response.getTracks().stream().forEach(track->{
               trackDtos.add(new TrackDto().builder()
                               .id(track.getId())
                               .trackDescription(track.getDescription())
                               .trackName(track.getTitle())
                               .trackUrl(track.getTrackRecord().getPath())
                               .trackDuration(track.getTrackRecord().getDuration())
                       .build());
           });

           UserDto userDto= UserDto.builder()
                   .id(label.getOwnerUser().getId())
                   .fullName(label.getOwnerUser().getFullName())
                   .email(label.getOwnerUser().getEmail())
                   .gender(label.getOwnerUser().getGender())
                   .dob(label.getOwnerUser().getDob())
                   .build();




        return new LabelResponse().builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .ownerUser(userDto)
                .albums(albumDtos)
                .tracks(trackDtos)
                .build();
    }
}
