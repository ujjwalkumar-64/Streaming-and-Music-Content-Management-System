package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.response.DTO.*;
import com.example.registrationProject.response.TrackResponse;
import com.example.registrationProject.service.CloudinaryService;
import com.example.registrationProject.service.RedisService;
import com.example.registrationProject.service.TrackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    TrackRecordRepository trackRecordRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    RedisService redisService;

    @Autowired
    TrackLikeRepository trackLikeRepository;

    @Autowired
    TrackStreamLogRepository trackStreamLogRepository;

    private TrackRecord uploadTrack(MultipartFile file) throws IOException, EncoderException {
        String filePath = "";
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());

              filePath= System.getProperty("user.dir")+"\\track"+ File.separator+ UUID.randomUUID().toString() +file.getOriginalFilename();


        FileOutputStream fout= new FileOutputStream(filePath);
        fout.write(file.getBytes());

        fout.flush();
        fout.close();

        File savedFile = new File(filePath);
        MultimediaObject multimediaObject= new MultimediaObject(savedFile);
        MultimediaInfo info= multimediaObject.getInfo();

         TrackRecord trackRecord = new TrackRecord();

         trackRecord.setName(file.getOriginalFilename());
         trackRecord.setSize(file.getSize());
         trackRecord.setDuration(info.getDuration());
         trackRecord.setMetaData(info.getMetadata().toString());
        trackRecord.setPath(filePath);
        trackRecord.setStatus(Status.active);
        trackRecord.setType(file.getContentType());

        trackRecordRepository.save(trackRecord);

        return trackRecord;

    }

    private String uploadPhoto(MultipartFile file) throws IOException {
        String filePath= System.getProperty("user.dir")+"\\pictures"+File.separator+ UUID.randomUUID().toString()+file.getOriginalFilename();
        FileOutputStream fout= new FileOutputStream(filePath);
        fout.write(file.getBytes());
        fout.flush();
        fout.close();
        return filePath;
    }


    @Transactional
    @Override
    public TrackResponse addTrack(TrackRequest trackRequest) {
        String redisKey1= "track:admin:list";
        if(redisService.exists(redisKey1)){
            redisService.delete(redisKey1);
        }
        try{
            if(trackRequest.getCoverImage().isEmpty() || trackRequest.getFile().isEmpty()){
                throw new CustomException("Track image or file is empty");
            }


            String profileUrl= cloudinaryService.uploadPhoto(trackRequest.getCoverImage());
            TrackRecord trackRecord = cloudinaryService.uploadTrackFile(trackRequest.getFile());

            List<Artist> artists= artistRepository.findAllById(trackRequest.getArtistIds());
             if(artists.size() != trackRequest.getArtistIds().size()){
                 throw new CustomException("All Artist Ids are not found some may be invalid");
             }

             List<Genre> genres= genreRepository.findAllById(trackRequest.getGenreIds());
             if(genres.size() != trackRequest.getGenreIds().size()){
                 throw  new CustomException("All Genre Ids are not found some may be invalid");
             }
             Album album= null;
            if(trackRequest.getAlbumId() != null){
                album= albumRepository.findById(trackRequest.getAlbumId()).orElseThrow(()->new CustomException("Album not found"));
            }

            Label label= null;
            if(trackRequest.getLabelId() != null){

              label= labelRepository.findById(trackRequest.getLabelId()).orElseThrow(()->new CustomException("Label not found"));
            }

            Language language= null;
            if(trackRequest.getLanguageId() != null){
                language= languageRepository.findById(trackRequest.getLanguageId()).orElseThrow(()->new CustomException("Language not found"));
            }


            Track track=new Track();

            track.setTitle(trackRequest.getTitle());
            track.setDescription(trackRequest.getDescription());
            track.setCoverImage(profileUrl);
            track.setTrackRecord(trackRecord);
            track.setStatus(Status.active);
            track.setAlbum(album);
            track.setLabel(label);
            track.setArtists(artists);
            track.setGenres(genres);
            track.setLanguage(language);


            Track response = trackRepository.save(track);

            LabelDto labelDto= new LabelDto();
            if(response.getLabel() != null){
                 labelDto.builder().
                        id(response.getLabel().getId())
                        .name(response.getLabel().getName())
                        .build();
            }

            AlbumDto albumDto= new AlbumDto();
          if(response.getAlbum() != null){
              albumDto.builder()
                      .id(response.getAlbum().getId())
                      .title(response.getAlbum().getTitle())
                      .build();

          }

          LanguageDto languageDto= new LanguageDto();
          if(response.getLanguage() != null){
              languageDto.builder()
                      .id(response.getLanguage().getId())
                      .language(response.getLanguage().getName())
                      .build();
          }


            List<GenreDto> genreDtos= new ArrayList<>();
          if(response.getGenres() != null){
              response.getGenres().stream().forEach(genre->{
                  genreDtos.add(new GenreDto().builder()
                          .id(genre.getId())
                          .name(genre.getName())

                          .build());
              });
          }

          List<ArtistDto> artistDtos= new ArrayList<>();
          if(response.getArtists() != null){
              response.getArtists().stream().forEach(artist->{
                  artistDtos.add(new ArtistDto().builder()
                          .id(artist.getId())
                          .artistName(artist.getArtistName())
                          .bio(artist.getBio())
                          .profilePic(artist.getProfilePic())
                          .build());

              });

          }

          String redisKey="track:id:"+response.getId();

            TrackResponse result=  new TrackResponse().builder()
                    .id(response.getId())
                    .trackDescription(response.getDescription())
                    .trackUrl(response.getCoverImage())
                    .trackCoverImage(response.getCoverImage())
                    .trackName(response.getTitle())
                    .trackDuration(response.getTrackRecord().getDuration())
                    .label(labelDto)
                    .album(albumDto)
                    .genre(genreDtos)
                    .artist(artistDtos)
                    .language(languageDto)
                    .build();

            redisService.saveWithTTL(redisKey,result,10,TimeUnit.MINUTES);

            return result;

        }
        catch(Exception e){
            throw   new CustomException("Failed to add track "+ e.getMessage());
        }

    }

    // need to work on validation for artist and label

    @Override
    public TrackResponse updateTrack(TrackRequest trackRequest, Long id) {

        String redisKey="track:id"+id;
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Track track=trackRepository.findById(id).orElseThrow(()->new CustomException("Invalid Track Id"));
        try{
            if(user.getRole().getRole().equalsIgnoreCase("ROLE_ARTIST")){
                track.getArtists().stream().filter((artist -> artist.getUser().getId().equals(user.getId()))).findFirst().orElseThrow(()-> new CustomException("You are not authorize to update"));
            }
            if(user.getRole().getRole().equals("ROLE_LABEL")){
                if(!track.getLabel().getOwnerUser().getId().equals(user.getId())){
                    throw new CustomException("You are not authorize to update");
                };
            }
            if(trackRequest.getCoverImage() != null ){
                String profileUrl = cloudinaryService.uploadPhoto(trackRequest.getCoverImage());
                track.setCoverImage(profileUrl);
            }
            if(trackRequest.getFile() != null){
                TrackRecord trackRecord = cloudinaryService.uploadTrackFile(trackRequest.getFile());
                track.setTrackRecord(trackRecord);
            }
            if(trackRequest.getDescription() != null){
                track.setDescription(trackRequest.getDescription());
            }
            if(trackRequest.getTitle() != null){
                track.setTitle(trackRequest.getTitle());
            }
            if (trackRequest.getArtistIds() != null) {
                List<Artist> artists= artistRepository.findAllById(trackRequest.getArtistIds());
                if(artists.size() != trackRequest.getArtistIds().size()){
                    throw new CustomException("All Artist Ids are not found some may be invalid");
                }
                track.setArtists(artists);
            }
            if(trackRequest.getGenreIds() != null) {
                List<Genre> genres= genreRepository.findAllById(trackRequest.getGenreIds());
                if(genres.size() != trackRequest.getGenreIds().size()){
                    throw new CustomException("All Genre Ids are not found some may be invalid");
                }
                track.setGenres(genres);
            }
            if (trackRequest.getAlbumId() !=null){
                Album album= albumRepository.findById(trackRequest.getAlbumId()).orElseThrow(()->new CustomException("Album not found"));
                track.setAlbum(album);
            }
            if (trackRequest.getLabelId() !=null){
                Label label= labelRepository.findById(trackRequest.getLabelId()).orElseThrow(()->new CustomException("Label not found"));
                track.setLabel(label);
            }

            if(trackRequest.getLanguageId() !=null){
                Language language= languageRepository.findById(trackRequest.getLanguageId()).orElseThrow(()->new CustomException("Language not found"));
                track.setLanguage(language);
            }

            Track response = trackRepository.save(track);

            if(redisService.exists(redisKey)){
                redisService.delete(redisKey);
            }

            LabelDto labelDto= new LabelDto().builder().
                    id(response.getLabel().getId())
                    .name(response.getLabel().getName())
                    .build();

            AlbumDto albumDto= new AlbumDto();
            if(response.getAlbum() != null){
                  albumDto.builder()
                        .id(response.getAlbum().getId())
                        .title(response.getAlbum().getTitle())
                        .build();
            }

            LanguageDto languageDto= new LanguageDto();
            if(response.getLanguage() != null){
                languageDto.builder()
                        .id(response.getLanguage().getId())
                        .language(response.getLanguage().getName())
                        .build();
            }


            List<GenreDto> genreDtos= new ArrayList<>();
            response.getGenres().stream().forEach(genre->{
                genreDtos.add(new GenreDto().builder()
                        .id(genre.getId())
                        .name(genre.getName())

                        .build());
            });

            List<ArtistDto> artistDtos= new ArrayList<>();
            response.getArtists().stream().forEach(artist->{
                artistDtos.add(new ArtistDto().builder()
                        .id(artist.getId())
                        .artistName(artist.getArtistName())
                        .build());

            });


            TrackResponse result=  new TrackResponse().builder()
                    .id(response.getId())
                    .trackDescription(response.getDescription())
                    .trackUrl(response.getTrackRecord().getPath())
                    .trackCoverImage(response.getCoverImage())
                    .trackName(response.getTitle())
                    .trackDuration(response.getTrackRecord().getDuration())
                    .label(labelDto)
                    .album(albumDto)
                    .genre(genreDtos)
                    .artist(artistDtos)
                    .language(languageDto)
                    .build();

            redisService.saveWithTTL(redisKey,result,10,TimeUnit.MINUTES);
            return result;

        }
        catch(Exception e){
            throw   new CustomException("Failed to update track "+ e.getMessage());
        }


    }


    @Override
    public TrackResponse getTrackById(Long id) throws CustomException {

        String redisKey= "track:id:"+id;
        TrackResponse cachedResponse = (TrackResponse) redisService.get(redisKey);

        if(cachedResponse != null){
            return cachedResponse;
        }

        Track response= trackRepository.findById(id).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        if(response.getStatus().equals(Status.inactive)){
            throw  new CustomException("Track not available");
        }
        LabelDto labelDto= new LabelDto().builder().
                id(response.getLabel().getId())
                .name(response.getLabel().getName())
                .build();

        AlbumDto albumDto= new AlbumDto();
        if(response.getAlbum() != null){
            albumDto.builder()
                    .id(response.getAlbum().getId())
                    .title(response.getAlbum().getTitle())
                    .build();
        }

        LanguageDto languageDto= new LanguageDto();
        if(response.getLanguage() != null){
            languageDto.builder()
                    .id(response.getLanguage().getId())
                    .language(response.getLanguage().getName())
                    .build();
        }

        List<GenreDto> genreDtos= new ArrayList<>();
        response.getGenres().stream().forEach(genre->{
            genreDtos.add(new GenreDto().builder()
                    .id(genre.getId())
                    .name(genre.getName())

                    .build());
        });

        List<ArtistDto> artistDtos= new ArrayList<>();
        response.getArtists().stream().forEach(artist->{
            artistDtos.add(new ArtistDto().builder()
                    .id(artist.getId())
                    .artistName(artist.getArtistName())
                    .build());

        });


        TrackResponse result =   new TrackResponse().builder()
                    .id(response.getId())
                    .trackDescription(response.getDescription())
                    .trackCoverImage(response.getCoverImage())
                    .trackUrl(response.getTrackRecord().getPath())
                    .trackName(response.getTitle())
                    .trackDuration(response.getTrackRecord().getDuration())
                    .label(labelDto)
                    .album(albumDto)
                    .genre(genreDtos)
                    .artist(artistDtos)
                    .language(languageDto)
                    .build();

        redisService.saveWithTTL(redisKey,result,10,TimeUnit.MINUTES);
        return result;
    }

    @Override
    public TrackResponse getTrackByName(String name) throws CustomException {
        Track response= trackRepository.findByName(name).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        if(response.getStatus().equals(Status.inactive)){
            throw  new CustomException("Track not available");
        }

        LabelDto labelDto= new LabelDto().builder().
                id(response.getLabel().getId())
                .name(response.getLabel().getName())
                .build();

        AlbumDto albumDto= new AlbumDto();
        if(response.getAlbum() != null){
            albumDto.builder()
                    .id(response.getAlbum().getId())
                    .title(response.getAlbum().getTitle())
                    .build();
        }

        List<GenreDto> genreDtos= new ArrayList<>();
        response.getGenres().stream().forEach(genre->{
            genreDtos.add(new GenreDto().builder()
                    .id(genre.getId())
                    .name(genre.getName())

                    .build());
        });

        List<ArtistDto> artistDtos= new ArrayList<>();
        response.getArtists().stream().forEach(artist->{
            artistDtos.add(new ArtistDto().builder()
                    .id(artist.getId())
                    .artistName(artist.getArtistName())
                    .build());

        });

        LanguageDto languageDto= new LanguageDto();
        if(response.getLanguage() != null){
            languageDto.builder()
                    .id(response.getLanguage().getId())
                    .language(response.getLanguage().getName())
                    .build();
        }



        return  new TrackResponse().builder()
                .id(response.getId())
                .trackDescription(response.getDescription())
                .trackUrl(response.getCoverImage())
                .trackName(response.getTitle())
                .trackDuration(response.getTrackRecord().getDuration())
                .label(labelDto)
                .album(albumDto)
                .genre(genreDtos)
                .artist(artistDtos)
                .language(languageDto)
                .build();

    }
    @Override
    public TrackResponse getTrackByGenre(TrackRequest trackRequest) {
        return new TrackResponse();
    }

    @Override
    public TrackResponse getTrackByLabel(TrackRequest trackRequest) {
        return new TrackResponse();

    }

    @Override
    public List<TrackResponse> getAllTracks() {

        String redisKey= "track:admin:list";

        List<TrackResponse> cachedResponse= (List<TrackResponse>) redisService.get(redisKey);

        if(cachedResponse!=null && !cachedResponse.isEmpty()){
            return cachedResponse;
        }

        List<Track>tracks= trackRepository.findAll();
        List<TrackResponse> trackResponses= new ArrayList<>();


        tracks.forEach(track->{

            List<ArtistDto> artists= new ArrayList<>();
            AlbumDto album = new AlbumDto();

            if(track.getAlbum() != null){
                album.setId(track.getAlbum().getId());
                album.setTitle(track.getAlbum().getTitle());
            }

            LabelDto label= new LabelDto();
            if(track.getLabel() != null){
                label.setName(track.getLabel().getName());
                label.setId(track.getLabel().getId());
            }

            LanguageDto language= new LanguageDto();
            if(track.getLanguage() != null){
                language.setId(track.getLanguage().getId());
                language.setLanguage(track.getLanguage().getName());
            }


            List<GenreDto> genres= new ArrayList<>();

            track.getGenres().forEach(genre->{
                genres.add(new GenreDto().builder()
                                .id(genre.getId())
                                .name(genre.getName())
                        .build());
            });




            track.getArtists().forEach(artist->{
                artists.add(new ArtistDto().builder()
                                .id(artist.getId())
                                .artistName(artist.getArtistName())
                        .build());
            });
            trackResponses.add(new TrackResponse().builder()
                            .id(track.getId())
                            .trackDuration(track.getTrackRecord().getDuration())
                            .trackUrl(track.getTrackRecord().getPath())
                            .trackDescription(track.getDescription())
                            .trackName(track.getTitle())
                            .album(album)
                            .trackType(track.getTrackRecord().getType())
                            .artist(artists)
                            .label(label)
                            .genre(genres)
                            .language(language)
                            .trackCoverImage(track.getCoverImage())

                    .build());
        });

        redisService.saveWithTTL(redisKey,trackResponses,10,TimeUnit.MINUTES);

        return trackResponses;
    }

    @Override
    public List<TrackResponse> getAllTracksForUser(){

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="track:browsing:user:email" + user.getEmail();

        List<TrackResponse> cachedUserBrowsingTracks= (List<TrackResponse>) redisService.get(redisKey);
        if(cachedUserBrowsingTracks!=null  && ! cachedUserBrowsingTracks.isEmpty()){
            return cachedUserBrowsingTracks;
        }

        List<Track>tracks= trackRepository.findAll();
        List<TrackResponse> trackResponses= new ArrayList<>();

        tracks.stream().filter((track -> track.getStatus().equals(Status.active)))
                .filter(track->
                        {
                            if(! user.getUserLanguages().isEmpty()){
                                 if(track.getLanguage() !=null &&  user.getUserLanguages().contains(track.getLanguage())){
                                     return true;
                                 };
                                 return false;
                            }
                            return true;
                        }
                )
                .forEach(track->{

            List<ArtistDto> artists= new ArrayList<>();
            AlbumDto album = new AlbumDto();

            if(track.getAlbum() != null){
                album.setId(track.getAlbum().getId());
                album.setTitle(track.getAlbum().getTitle());
            }

            LabelDto label= new LabelDto();
            if(track.getLabel() != null){
                label.setName(track.getLabel().getName());
                label.setId(track.getLabel().getId());
            }



            List<GenreDto> genres= new ArrayList<>();

            track.getGenres().forEach(genre->{
                genres.add(new GenreDto().builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build());
            });


            track.getArtists().forEach(artist->{
                artists.add(new ArtistDto().builder()
                        .id(artist.getId())
                        .artistName(artist.getArtistName())
                        .build());
            });

            LanguageDto language= new LanguageDto();
            if(track.getLanguage() != null){
                language.setId(track.getLanguage().getId());
                language.setLanguage(track.getLanguage().getName());
            }

            trackResponses.add(new TrackResponse().builder()
                    .id(track.getId())
                    .trackDuration(track.getTrackRecord().getDuration())
                    .trackUrl(track.getTrackRecord().getPath())
                    .trackDescription(track.getDescription())
                    .trackName(track.getTitle())
                    .album(album)
                    .trackType(track.getTrackRecord().getType())
                    .artist(artists)
                    .label(label)
                    .genre(genres)
                    .language(language)
                            .trackCoverImage(track.getCoverImage())

                    .build());
        });

        redisService.saveWithTTL(redisKey,trackResponses,10, TimeUnit.MINUTES);

        return trackResponses;
    }



    @Override
    public void deleteTrackById(Long id) {
        Track track= trackRepository.findById(id).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
         track.setStatus(Status.inactive);
         trackRepository.save(track);

    }

    @Override
    public void trackLike(Long trackId){
        Track track= trackRepository.findById(trackId).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="tracks:liked:user:"+ user.getId();
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }

        if(trackLikeRepository.existsByTrackIdAndUserIdAndStatus(trackId, user.getId(),Status.active)){
            throw new CustomException("ALREADY LIKED");
        }

        if(trackLikeRepository.existsByTrackIdAndUserIdAndStatus(trackId, user.getId(),Status.inactive)){
            TrackLike trackLike= trackLikeRepository.findByTrackIdAndUserId(trackId,user.getId()).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
            trackLike.setStatus(Status.active);
            trackLikeRepository.save(trackLike);
        }
        else{
            TrackLike trackLike= new TrackLike();
            trackLike.setUser(user);
            trackLike.setTrack(track);
            trackLike.setStatus(Status.active);
            trackLikeRepository.save(trackLike);
        }



    }

    @Override
    public void trackUnlike(Long trackId){
        Track track= trackRepository.findById(trackId).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="tracks:liked:user:"+ user.getId();
        if(redisService.exists(redisKey)){
            redisService.delete(redisKey);
        }
        if(trackLikeRepository.existsByTrackIdAndUserIdAndStatus(trackId, user.getId(),Status.inactive)){
            throw new CustomException("ALREADY UNLIKED");
        }

        TrackLike trackLike= trackLikeRepository.findByTrackIdAndUserId(track.getId(), user.getId()).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        trackLike.setStatus(Status.inactive);
        trackLikeRepository.save(trackLike);
    }


    @Override
    public List<TrackResponse> getAllLikedTracksByUserId(){

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String redisKey="tracks:liked:user:"+user.getId();
        List<TrackResponse> cachedResponse= (List<TrackResponse>) redisService.get(redisKey);
        if(cachedResponse!=null && !cachedResponse.isEmpty()){
            return cachedResponse;
        }

        List<Track> likedTrack= trackLikeRepository.findLikedTracksByUser(user.getId());

        List<TrackResponse> trackResponses= new ArrayList<>();
        likedTrack.forEach(track->{

            List<ArtistDto> artists= new ArrayList<>();

            LanguageDto language= new LanguageDto().builder()
                    .id(track.getLanguage().getId())
                    .language(track.getLanguage().getName())
                    .build();

            List<GenreDto> genres= new ArrayList<>();

            track.getGenres().forEach(genre->{
                genres.add(new GenreDto().builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build());
            });




            track.getArtists().forEach(artist->{
                artists.add(new ArtistDto().builder()
                        .id(artist.getId())
                        .artistName(artist.getArtistName())
                        .build());
            });

            AlbumDto album = new AlbumDto();

            if(track.getAlbum() != null){
                album.setId(track.getAlbum().getId());
                album.setTitle(track.getAlbum().getTitle());
            }

            LabelDto label= new LabelDto();
            if(track.getLabel() != null){
                label.setName(track.getLabel().getName());
                label.setId(track.getLabel().getId());
            }


            trackResponses.add(new TrackResponse().builder()
                            .id(track.getId())
                            .trackCoverImage(track.getCoverImage())
                            .trackDescription(track.getDescription())
                            .trackName(track.getTitle())
                            .trackUrl(track.getTrackRecord().getPath())
                            .trackType(track.getTrackRecord().getType())
                            .artist(artists)
                            .genre(genres)
                            .album(album)
                            .label(label)
                            .trackDuration(track.getTrackRecord().getDuration())
                            .language(language)
                    .build()
            );
        });

        redisService.saveWithTTL(redisKey,trackResponses,10, TimeUnit.MINUTES);

        return trackResponses;

    }




    @Override
    public void trackStreamLog(Long trackId, HttpServletRequest httpServletRequest ) {
        Track track= trackRepository.findById(trackId).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TrackStreamLog trackStreamLog= new TrackStreamLog();

        System.out.println(httpServletRequest.getRemoteAddr());
        System.out.println(httpServletRequest.getRemoteUser());
        System.out.println(httpServletRequest.getRemoteHost());

        trackStreamLog.setUser(user);
        trackStreamLog.setTrack(track);
        trackStreamLog.setStatus(Status.active);
        trackStreamLog.setDeviceIp(httpServletRequest.getRemoteAddr());
        trackStreamLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        trackStreamLogRepository.save(trackStreamLog);

    }

    @Override
    public Integer countLikedTracksByUserId(Long userId){
        String redisKey="track:streamCount:liked:user:"+ userId;

        Integer cachedResponse= (Integer) redisService.get(redisKey);
         if(cachedResponse !=null){
             return cachedResponse;
         }

        Integer result=  trackLikeRepository.countLikedTracksByUserId( userId);
         redisService.saveWithTTL(redisKey,result,2, TimeUnit.MINUTES);
         return result;
    }

    @Override

    public Integer countUserLikedByTrackId(Long trackId){
        String redisKey="track:streamCount:user:"+ trackId;

        Integer cachedResponse= (Integer) redisService.get(redisKey);
        if(cachedResponse !=null){
            return cachedResponse;
        }

        Integer result= trackLikeRepository.countLikedTracksByTrackIdId(trackId);
        redisService.saveWithTTL(redisKey,result,2, TimeUnit.MINUTES);
        return result;

    }

    public List<TrendingDto> getTrendingTracks() {

        String redisKey = "trending:tracks:lists";

        List<TrendingDto> result = (List<TrendingDto>) redisService.get(redisKey);
        if (result != null) {
            return result;
        }


        List<TrackTrendDto> trackTrendDtos = trackRepository.findAllTrendingTrackIds();


        Map<Long, TrackTrendDto> trendMap = trackTrendDtos.stream()
                .collect(Collectors.toMap(TrackTrendDto::getTrackId, Function.identity()));

        List<Long> trackIds = new ArrayList<>(trendMap.keySet());


        List<Track> tracks = trackRepository.findAllById(trackIds);

        if (tracks.isEmpty()) {
            throw new CustomException("TRACK NOT FOUND");
        }


        List<TrendingDto> trendingDtos = new ArrayList<>();
        for (Track track : tracks) {


            List<ArtistDto> artistDtos = new ArrayList<>();
            if (track.getArtists() != null) {
                for (Artist artist : track.getArtists()) {
                    artistDtos.add(ArtistDto.builder()
                            .id(artist.getId())
                            .artistName(artist.getArtistName())
                            .build());
                }
            }

            AlbumDto albumDto = null;
            if (track.getAlbum() != null) {
                albumDto = new AlbumDto();
                albumDto.setId(track.getAlbum().getId());
                albumDto.setTitle(track.getAlbum().getTitle());
            }

            List<GenreDto> genreDtos = new ArrayList<>();
            if (track.getGenres() != null) {
                for (Genre genre : track.getGenres()) {
                    genreDtos.add(GenreDto.builder()
                            .id(genre.getId())
                            .name(genre.getName())
                            .build());
                }
            }

            TrackDto trackDto = TrackDto.builder()
                    .id(track.getId())
                    .coverImage(track.getCoverImage())
                    .trackName(track.getTitle())
                    .trackDescription(track.getDescription())
                    .trackDuration(track.getTrackRecord().getDuration())
                    .trackUrl(track.getTrackRecord().getPath())
                    .artists(artistDtos)
                    .album(albumDto)
                    .genres(genreDtos)
                    .build();


            int streamCount = trendMap.get(track.getId()).getCountStream();
            int trackLikedCount= trendMap.get(track.getId()).getCountLike();
            int trendingCount= trendMap.get(track.getId()).getCalcTrend();


            trendingDtos.add(new TrendingDto(trackDto, streamCount, trackLikedCount,trendingCount));
        }


        redisService.saveWithTTL(redisKey, trendingDtos, 10, TimeUnit.MINUTES);

        return trendingDtos;
    }


}



