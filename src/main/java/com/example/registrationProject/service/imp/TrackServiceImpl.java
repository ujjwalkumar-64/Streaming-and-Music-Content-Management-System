package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.TrackRequest;
import com.example.registrationProject.response.DTO.*;
import com.example.registrationProject.response.TrackResponse;
import com.example.registrationProject.service.TrackService;
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
import java.util.UUID;

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

    private TrackRecord uploadTrack(MultipartFile file) throws IOException, EncoderException {
        String filePath = "";
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());

              filePath= System.getProperty("user.dir")+"\\tracks"+ File.separator+ UUID.randomUUID().toString() +file.getOriginalFilename();


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



    @Override
    public TrackResponse addTrack(TrackRequest trackRequest) {
        try{
            User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(trackRequest.getCoverImage().isEmpty() || trackRequest.getFile().isEmpty()){
                throw new CustomException("Track image or file is empty");
            }

            String profileUrl = uploadPhoto(trackRequest.getCoverImage());
            TrackRecord trackRecord = uploadTrack(trackRequest.getFile());

           switch(user.getRole().getRole()) {
               case "ROLE_ARTIST" -> {
                   Artist artist = artistRepository.findByUserId(user.getId()).orElseThrow(()->new CustomException("Artist not found"));
                   if(!trackRequest.getArtistIds().contains(artist.getId())) {
                       throw new CustomException("You can submit your own track");
                   }
               }

               case "ROLE_LABEL" ->{
                   Label label = labelRepository.findByUserId(user.getId()).orElseThrow(()->new CustomException("Label not found"));
                   if(!trackRequest.getLabelId().equals(label.getId())) {
                       throw new CustomException("You can submit your own track");
                   }
               }
           }

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
            if(trackRequest.getAlbumId() != null){

              label= labelRepository.findById(trackRequest.getLabelId()).orElseThrow(()->new CustomException("Label not found"));
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


            Track response = trackRepository.save(track);

          LabelDto labelDto= new LabelDto().builder().
                  id(response.getLabel().getId())
                  .name(response.getLabel().getName())
                  .description(response.getLabel().getDescription())
                  .build();

          AlbumDto albumDto= new AlbumDto().builder()
                  .id(response.getAlbum().getId())
                  .title(response.getAlbum().getTitle())
                  .description(response.getAlbum().getDescription())
                  .releaseDate(response.getAlbum().getReleaseDate())
                  .build();

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
                        .bio(artist.getBio())
                        .profilePic(artist.getProfilePic())
                        .build());

            });




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
                    .build();

        }
        catch(Exception e){
            throw   new CustomException("Failed to add track "+ e.getMessage());
        }

    }

    // need to work on validation for artist and label

    @Override
    public TrackResponse updateTrack(TrackRequest trackRequest) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Track track=trackRepository.findById(trackRequest.getId()).orElseThrow(()->new CustomException("Invalid Track Id"));
        try{
            if(trackRequest.getCoverImage() != null ){
                String profileUrl = uploadPhoto(trackRequest.getCoverImage());
                track.setCoverImage(profileUrl);
            }
            if(trackRequest.getFile() != null){
                TrackRecord trackRecord = uploadTrack(trackRequest.getFile());
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

            Track response = trackRepository.save(track);
            LabelDto labelDto= new LabelDto().builder().
                    id(response.getLabel().getId())
                    .name(response.getLabel().getName())
                    .description(response.getLabel().getDescription())
                    .build();

            AlbumDto albumDto= new AlbumDto().builder()
                    .id(response.getAlbum().getId())
                    .title(response.getAlbum().getTitle())
                    .description(response.getAlbum().getDescription())
                    .releaseDate(response.getAlbum().getReleaseDate())
                    .build();

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
                        .bio(artist.getBio())
                        .profilePic(artist.getProfilePic())
                        .build());

            });




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
                    .build();

        }
        catch(Exception e){
            throw   new CustomException("Failed to update track "+ e.getMessage());
        }




    }


    @Override
    public TrackResponse getTrackById(Long id) {
        Track response= trackRepository.findById(id).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));

        LabelDto labelDto= new LabelDto().builder().
                id(response.getLabel().getId())
                .name(response.getLabel().getName())
                .description(response.getLabel().getDescription())
                .build();

        AlbumDto albumDto= new AlbumDto().builder()
                .id(response.getAlbum().getId())
                .title(response.getAlbum().getTitle())
                .description(response.getAlbum().getDescription())
                .releaseDate(response.getAlbum().getReleaseDate())
                .build();

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
                    .bio(artist.getBio())
                    .profilePic(artist.getProfilePic())
                    .build());

        });




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
                .build();

    }

    @Override
    public TrackResponse getTrackByName(String name) {
        Track response= trackRepository.findByName(name).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        LabelDto labelDto= new LabelDto().builder().
                id(response.getLabel().getId())
                .name(response.getLabel().getName())
                .description(response.getLabel().getDescription())
                .build();

        AlbumDto albumDto= new AlbumDto().builder()
                .id(response.getAlbum().getId())
                .title(response.getAlbum().getTitle())
                .description(response.getAlbum().getDescription())
                .releaseDate(response.getAlbum().getReleaseDate())
                .build();

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
                    .bio(artist.getBio())
                    .profilePic(artist.getProfilePic())
                    .build());

        });




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
    public List<TrackDto> getAllTracks() {
        List<Track>tracks= trackRepository.findAll();
        List<TrackDto> trackDtos= new ArrayList<>();

        tracks.stream().forEach(track->{
            trackDtos.add(new TrackDto().builder()
                            .id(track.getId())
                            .trackDuration(track.getTrackRecord().getDuration())
                            .trackUrl(track.getTrackRecord().getPath())
                            .trackDescription(track.getDescription())
                            .trackName(track.getTitle())
                    .build());
        });

        return trackDtos;
    }

    @Override
    public void deleteTrackById(TrackRequest trackRequest) {
        Track track= trackRepository.findById(trackRequest.getId()).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
        trackRepository.deleteById(track.getId());

    }

    @Override
    public void deleteTrackByName(TrackRequest trackRequest) {
         Track track= trackRepository.findByName(trackRequest.getTitle()).orElseThrow(()-> new CustomException("TRACK NOT FOUND"));
         trackRepository.deleteById(track.getId());
    }

}



