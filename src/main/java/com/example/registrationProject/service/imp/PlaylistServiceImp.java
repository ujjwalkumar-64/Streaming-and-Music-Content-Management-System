package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.PlaylistRepository;
import com.example.registrationProject.repository.TrackRepository;
import com.example.registrationProject.request.PlaylistRequest;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.PlaylistResponse;
import com.example.registrationProject.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        playlist.setType(Type.Private);
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
    public List<PlaylistResponse> getUserPlaylist() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Playlist> playlists= playlistRepository.findPlaylistByCreatorId(user.getId());

        UserDto userDto= new UserDto().builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .build();

        List<PlaylistResponse> playlistsResponse= new ArrayList<>();
          playlists.forEach(playlist->{
              List<TrackDto> trackDtos= new ArrayList<>();
              playlist.getTracks().forEach(track->{
                  trackDtos.add(new TrackDto().builder()
                          .id(track.getId())
                          .trackName(track.getTitle())
                          .trackUrl(track.getTrackRecord().getPath())
                          .build());
              });
            playlistsResponse.add(new PlaylistResponse().builder()
                            .id(playlist.getId())
                            .name(playlist.getName())
                            .description(playlist.getDescription())
                            .status(playlist.getStatus())
                            .creator(userDto)
                            .tracks(trackDtos)

                    .build());
        });

          return playlistsResponse;

    }

    @Override
    public List<PlaylistResponse> getAllPlaylists() {
         List<Playlist> playlists= playlistRepository.findAll();
         List<PlaylistResponse> playlistsResponse= new ArrayList<>();

         playlists.forEach(playlist->{
             List<TrackDto> trackDtos= new ArrayList<>();
             playlist.getTracks().forEach(track->{
                    trackDtos.add(new TrackDto().builder()
                                    .id(track.getId())
                                    .trackName(track.getTitle())
                                    .trackUrl(track.getTrackRecord().getPath())
                            .build());
                });

                UserDto userDto = new UserDto().builder()
                        .id(playlist.getCreator().getId())
                        .fullName(playlist.getCreator().getFullName())
                        .build();

             playlistsResponse.add(new PlaylistResponse().builder()
                             .id(playlist.getId())
                             .name(playlist.getName())
                             .status(playlist.getStatus())
                             .description(playlist.getDescription())
                             .creator(userDto)
                             .tracks(trackDtos)
                     .build());
         });

         return playlistsResponse;

    }


}
