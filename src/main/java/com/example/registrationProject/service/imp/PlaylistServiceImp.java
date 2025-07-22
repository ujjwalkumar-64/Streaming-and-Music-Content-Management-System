package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Playlist;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.PlaylistRepository;
import com.example.registrationProject.repository.TrackRepository;
import com.example.registrationProject.request.PlaylistRequest;
import com.example.registrationProject.response.DTO.AlbumDto;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.PlaylistResponse;
import com.example.registrationProject.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlaylistServiceImp   implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Override
    public PlaylistResponse createPlaylist(PlaylistRequest playlistRequest) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Playlist playlist = new Playlist();
        playlist.setDescription(playlistRequest.getDescription());
        playlist.setCreator(user);
        if(playlistRepository.existsByNameAndCreator_Id(playlistRequest.getName(),user.getId())){
            throw new CustomException("Playlist name Already Exists for you change name of playlist");
        }
        playlist.setName(playlistRequest.getName());

        List<Track> tracks= trackRepository.findAllById(playlistRequest.getTrackIds());
        if(tracks.size() != playlistRequest.getTrackIds().size()){
           throw new CustomException("All Track Ids are not found some may be invalid");
        }

        playlist.setTracks(tracks);
        playlist.setStatus(Status.active);
        Playlist response= playlistRepository.save(playlist);


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
                .id(playlist.getCreator().getId())
                .fullName(playlist.getCreator().getFullName())
                .email(playlist.getCreator().getEmail())
                .gender(playlist.getCreator().getGender())
                .dob(playlist.getCreator().getDob())
                .build();



        return  new PlaylistResponse().builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .creator(userDto)
                .tracks(trackDtos)
                .status(response.getStatus())
                .build();


    }


    @Override
    public PlaylistResponse addTrackToPlaylist(PlaylistRequest playlistRequest) {
        Playlist playlist = playlistRepository.findById(playlistRequest.getId()).orElseThrow(()-> new CustomException("Playlist Not Found"));
         Track track1 =  trackRepository.findById(playlistRequest.getTrackId()).orElseThrow(() -> new CustomException("Track Not Found"));
         if(playlistRepository.checkTrackPresentInPlaylist(playlistRequest.getTrackId())){
             throw new CustomException("Track already present in playlist");
         };

         playlist.getTracks().add(track1);
         Playlist response= playlistRepository.save(playlist);

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
                .id(playlist.getCreator().getId())
                .fullName(playlist.getCreator().getFullName())
                .email(playlist.getCreator().getEmail())
                .gender(playlist.getCreator().getGender())
                .dob(playlist.getCreator().getDob())
                .build();



        return  new PlaylistResponse().builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .creator(userDto)
                .tracks(trackDtos)
                .status(response.getStatus())
                .build();

    }
}
