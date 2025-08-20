package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.PlaylistRepository;
import com.example.registrationProject.repository.TrackRepository;
import com.example.registrationProject.request.PlaylistRequest;
import com.example.registrationProject.response.DTO.AlbumDto;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.DTO.TrackDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.PlaylistResponse;
import com.example.registrationProject.service.PlaylistService;
import com.example.registrationProject.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PlaylistServiceImp   implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    RedisService redisService;

    @Override
    public PlaylistResponse createPlaylist(PlaylistRequest playlistRequest) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey= "playlist:user:email:"+user.getEmail();

        Playlist playlist = new Playlist();
        playlist.setDescription(playlistRequest.getDescription());
        playlist.setCreator(user);
        if(playlistRepository.existsByNameAndCreator_Id(playlistRequest.getName(),user.getId())){
            throw new CustomException("Playlist name Already Exists for you change name of playlist");
        }
        playlist.setName(playlistRequest.getName());

        if(playlistRequest.getTrackIds() != null && !playlistRequest.getTrackIds().isEmpty()){
            List<Track> tracks= trackRepository.findAllById(playlistRequest.getTrackIds());
            if(tracks.size() != playlistRequest.getTrackIds().size()){
                throw new CustomException("All Track Ids are not found some may be invalid");
            }

            playlist.setTracks(tracks);
        }


        playlist.setStatus(Status.active);
        playlist.setType(Type.Private);
        Playlist response= playlistRepository.save(playlist);


        List<TrackDto> trackDtos= new ArrayList<>();
        if(response.getTracks() != null){
            response.getTracks().stream().forEach(track->{
                trackDtos.add(new TrackDto().builder()
                        .id(track.getId())
                        .trackDescription(track.getDescription())
                        .trackName(track.getTitle())
                        .trackUrl(track.getTrackRecord().getPath())
                        .trackDuration(track.getTrackRecord().getDuration())
                        .build());
            });
        }


        UserDto userDto= UserDto.builder()
                .id(playlist.getCreator().getId())
                .fullName(playlist.getCreator().getFullName())
                .build();



        PlaylistResponse result=  new PlaylistResponse().builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .creator(userDto)
                .tracks(trackDtos)
                .status(response.getStatus())
                .type(response.getType())
                .build();

        redisService.saveWithTTL(redisKey,result,10, TimeUnit.MINUTES);
        return result;

    }


    @Override
    public PlaylistResponse addTrackToPlaylist(Long playlistId, Long trackId) {

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(()-> new CustomException("Playlist Not Found"));
         Track track1 =  trackRepository.findById(trackId).orElseThrow(() -> new CustomException("Track Not Found"));
         if(playlist.getTracks().contains(track1)) {
             throw new CustomException("Track already present in playlist");
         };

         playlist.getTracks().add(track1);
         Playlist response= playlistRepository.save(playlist);

        List<TrackDto> trackDtos= new ArrayList<>();

        if(response.getTracks() != null && !response.getTracks().isEmpty()){
        response.getTracks().forEach(track->{
            AlbumDto albumDto= new AlbumDto();

            if(track.getAlbum() != null){
                albumDto.builder()
                        .id(track.getAlbum().getId())
                        .title(track.getAlbum().getTitle())
                        .description(track.getAlbum().getDescription())
                        .build();
            }


            trackDtos.add(new TrackDto().builder()
                    .id(track.getId())
                    .trackDescription(track.getDescription())
                    .trackName(track.getTitle())
                    .trackUrl(track.getTrackRecord().getPath())
                    .trackDuration(track.getTrackRecord().getDuration())
                            .album(albumDto)
                    .build());
        });
        }

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
                .type(response.getType())
                .build();

    }

    @Override
    public List<PlaylistResponse> getUserPlaylist() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey= "playlist:user:email:"+user.getEmail();

        List<PlaylistResponse> cachedResponse= (List<PlaylistResponse>) redisService.get(redisKey);
        if(cachedResponse!=null && !cachedResponse.isEmpty()){
            return cachedResponse;
        }

        List<Playlist> playlists= playlistRepository.findPlaylistByCreatorId(user.getId());


        List<PlaylistResponse> playlistsResponse= new ArrayList<>();
          playlists.forEach(playlist->{
              UserDto userDto= new UserDto().builder()
                      .id(playlist.getCreator().getId())
                      .fullName(playlist.getCreator().getFullName())
                      .build();
              List<TrackDto> trackDtos= new ArrayList<>();
              playlist.getTracks().forEach(track->{
                 List<ArtistDto> artistDtos= new ArrayList<>();
                  track.getArtists().stream().forEach(artist->{
                      artistDtos.add(new ArtistDto().builder()
                                      .id(artist.getId())
                                      .artistName(artist.getArtistName())
                              .build());
                  });
                  AlbumDto albumDto= new AlbumDto();
                  if(track.getAlbum()!=null){
                      albumDto.builder()
                              .id(track.getAlbum().getId())
                              .title(track.getAlbum().getTitle())
                              .build();
                  }

                  trackDtos.add(new TrackDto().builder()
                          .id(track.getId())
                          .trackName(track.getTitle())
                          .trackUrl(track.getTrackRecord().getPath())
                                  .trackDuration(track.getTrackRecord().getDuration())
                                  .artists(artistDtos)
                                  .album(albumDto)
                          .build());
              });
            playlistsResponse.add(new PlaylistResponse().builder()
                            .id(playlist.getId())
                            .name(playlist.getName())
                            .description(playlist.getDescription())
                            .status(playlist.getStatus())
                            .creator(userDto)
                            .tracks(trackDtos)
                            .type(playlist.getType())

                    .build());
        });

          redisService.saveWithTTL(redisKey,playlistsResponse,10, TimeUnit.MINUTES);

          return playlistsResponse;

    }

    @Override
    public List<PlaylistResponse> getAllPlaylists() {

        String redisKey="playlist:admin:list:all";

        List<PlaylistResponse> cachedResponse= (List<PlaylistResponse>) redisService.get(redisKey);

        if(cachedResponse!=null && !cachedResponse.isEmpty()){
            return cachedResponse;
        }

         List<Playlist> playlists= playlistRepository.findAll();
         List<PlaylistResponse> playlistsResponse= new ArrayList<>();

         playlists.forEach(playlist->{

                UserDto userDto = new UserDto().builder()
                        .id(playlist.getCreator().getId())
                        .fullName(playlist.getCreator().getFullName())
                        .build();


                 List<TrackDto> trackDtos= new ArrayList<>();
                 if(playlist.getTracks() != null && !playlist.getTracks().isEmpty()){
                     playlist.getTracks().forEach(track->{
                         List<ArtistDto> artistDtos= new ArrayList<>();
                         track.getArtists().stream().forEach(artist->{
                             artistDtos.add(new ArtistDto().builder()
                                     .id(artist.getId())
                                     .artistName(artist.getArtistName())
                                     .build());
                         });
                         AlbumDto albumDto= new AlbumDto();
                         if(track.getAlbum()!=null){
                             albumDto.builder()
                                     .id(track.getAlbum().getId())
                                     .title(track.getAlbum().getTitle())
                                     .build();
                         }

                         trackDtos.add(new TrackDto().builder()
                                 .id(track.getId())
                                 .trackName(track.getTitle())
                                 .trackUrl(track.getTrackRecord().getPath())
                                 .trackDuration(track.getTrackRecord().getDuration())
                                 .artists(artistDtos)
                                 .album(albumDto)
                                 .build());
                     });
                 }


                 playlistsResponse.add(new PlaylistResponse().builder()
                         .id(playlist.getId())
                         .name(playlist.getName())
                         .description(playlist.getDescription())
                         .status(playlist.getStatus())
                         .creator(userDto)
                         .tracks(trackDtos)
                                 .type(playlist.getType())

                         .build());
             });

         redisService.saveWithTTL(redisKey,playlistsResponse,10, TimeUnit.MINUTES);

             return playlistsResponse;

    }


}
