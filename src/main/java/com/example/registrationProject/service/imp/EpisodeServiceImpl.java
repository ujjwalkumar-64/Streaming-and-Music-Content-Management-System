package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.ArtistRepository;
import com.example.registrationProject.repository.EpisodeRecordRepository;
import com.example.registrationProject.repository.EpisodeRepository;
import com.example.registrationProject.repository.LanguageRepository;
import com.example.registrationProject.request.EpisodeRequest;
import com.example.registrationProject.response.DTO.ArtistDto;
import com.example.registrationProject.response.EpisodeResponse;
import com.example.registrationProject.service.CloudinaryService;
import com.example.registrationProject.service.EpisodeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EpisodeServiceImpl implements EpisodeService {

    @Autowired
    EpisodeRecordRepository episodeRecordRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    private EpisodeRecord uploadEpisode(MultipartFile file) throws IOException, EncoderException {
        String filePath = "";
        System.out.println(file.getContentType());
        System.out.println(file.getOriginalFilename());

        filePath= System.getProperty("user.dir")+"\\episodes"+ File.separator+ UUID.randomUUID().toString() +file.getOriginalFilename();


        FileOutputStream fout= new FileOutputStream(filePath);
        fout.write(file.getBytes());

        fout.flush();
        fout.close();

        File savedFile = new File(filePath);
        MultimediaObject multimediaObject= new MultimediaObject(savedFile);
        MultimediaInfo info= multimediaObject.getInfo();

        EpisodeRecord episodeRecord = new EpisodeRecord();

        episodeRecord.setName(file.getOriginalFilename());
        episodeRecord.setSize(file.getSize());
        episodeRecord.setDuration(info.getDuration());
        episodeRecord.setMetaData(info.getMetadata().toString());
        episodeRecord.setPath(filePath);
        episodeRecord.setStatus(Status.active);
        episodeRecord.setType(file.getContentType());

        episodeRecordRepository.save(episodeRecord);

        return episodeRecord;

    }

    public String uploadPhoto(MultipartFile file) throws IOException {
        String filePath= System.getProperty("user.dir")+"\\pictures"+File.separator+ UUID.randomUUID().toString()+file.getOriginalFilename();
        FileOutputStream fout= new FileOutputStream(filePath);
        fout.write(file.getBytes());
        fout.flush();
        fout.close();
        return filePath;
    }

    @Override
    public EpisodeResponse addEpisode(EpisodeRequest episodeRequest) {
        try{
                if(episodeRequest.getCoverImage().isEmpty() || episodeRequest.getFile().isEmpty()){
                    throw new CustomException("Episode image or file is empty");
                }
                String coverUrl = cloudinaryService.uploadPhoto(episodeRequest.getCoverImage());
                EpisodeRecord episodeRecord = cloudinaryService.uploadEpisodeFile(episodeRequest.getFile());

            List<Artist> artists= artistRepository.findAllById(episodeRequest.getArtistIds());
            if(artists.size() != episodeRequest.getArtistIds().size()){
                throw new CustomException("All Artist Ids are not found some may be invalid");
            }

            List<ArtistDto> artistDtos= new ArrayList<>();
            artists.forEach(artist->{
                artistDtos.add(new ArtistDto().builder()
                                .id(artist.getId())
                                .artistName(artist.getArtistName())
                                .bio(artist.getBio())
                        .build());
            });


            if(episodeRequest.getLanguageId()==null){
                throw new CustomException("Language Id is empty");
            }

            Language language= languageRepository.findById(episodeRequest.getLanguageId()).orElseThrow(()->new CustomException("Language not found"));

            Episode episode = new Episode();
             episode.setTitle(episodeRequest.getTitle());
             episode.setCoverImage(coverUrl);
             episode.setDescription(episodeRequest.getDescription());
             episode.setArtists(artists);
             episode.setLanguage(language);
             episode.setStatus(Status.active);
             episode.setEpisodeRecord(episodeRecord);
             Episode response= episodeRepository.save(episode);

            return EpisodeResponse.builder()
                    .id(response.getId())
                    .title(response.getTitle())
                    .description(response.getDescription())
                    .coverImage(response.getCoverImage())
                    .createdAt(response.getCreatedAt())
                    .episodeUrl(response.getEpisodeRecord().getPath())
                    .language(response.getLanguage().getName())
                    .artists(artistDtos)
                    .type(response.getEpisodeRecord().getType())
                    .duration(episode.getEpisodeRecord().getDuration())
                    .build();

            }
            catch (Exception e){
                throw new CustomException(e.getMessage());
            }

    }

    @Override
    public EpisodeResponse updateEpisode(EpisodeRequest episodeRequest) {
        try{
            Episode episode = episodeRepository.findById(episodeRequest.getId()).orElseThrow(()->new CustomException("Episode not found"));

            if(episodeRequest.getCoverImage() != null ){
                String coverUrl = cloudinaryService.uploadPhoto(episodeRequest.getCoverImage());
                episode.setCoverImage(coverUrl);
            }
            if(episodeRequest.getFile() != null){
                EpisodeRecord episodeRecord =  cloudinaryService.uploadEpisodeFile(episodeRequest.getFile());
                episode.setEpisodeRecord(episodeRecord);
            }

            if(episodeRequest.getArtistIds() != null && !episodeRequest.getArtistIds().isEmpty()){
                List<Artist> artists = artistRepository.findAllById(episodeRequest.getArtistIds());
                if(artists.size() != episodeRequest.getArtistIds().size()){
                    throw new CustomException("All Artist Ids are not found some may be invalid");
                }
                episode.setArtists(artists);

            }

            if( episodeRequest.getDescription() !=null && !episodeRequest.getDescription().isEmpty()){
                episode.setDescription(episodeRequest.getDescription());
            }

            if( episodeRequest.getTitle()!=null && !episodeRequest.getTitle().isEmpty()){
                episode.setTitle(episodeRequest.getTitle());
            }

            if(episodeRequest.getLanguageId()!= null){
                Language language = languageRepository.findById(episodeRequest.getLanguageId()).orElseThrow(()->new CustomException("Language not found"));
                episode.setLanguage(language);
            }

            if( episodeRequest.getArtistIds() != null && !episodeRequest.getArtistIds().isEmpty()){
                List<Artist> artists = artistRepository.findAllById(episodeRequest.getArtistIds());
                if(artists.size() != episodeRequest.getArtistIds().size()){
                    throw new CustomException("All Artist Ids are not found some may be invalid");
                }
                episode.setArtists(artists);
            }

            Episode response= episodeRepository.save(episode);
            List<ArtistDto> artistDtos= new ArrayList<>();
            response.getArtists().forEach(artist->{
                artistDtos.add(new ArtistDto().builder()
                                .id(artist.getId())
                        .artistName(artist.getArtistName())
                        .bio(artist.getBio())
                        .build());
            });

            return EpisodeResponse.builder()
                    .id(response.getId())
                    .title(response.getTitle())
                    .description(response.getDescription())
                    .coverImage(response.getCoverImage())
                    .createdAt(response.getCreatedAt())
                    .episodeUrl(response.getEpisodeRecord().getPath())
                    .language(response.getLanguage().getName())
                    .artists(artistDtos)
                    .type(response.getEpisodeRecord().getType())
                    .duration(response.getEpisodeRecord().getDuration())
                    .build();

        }
        catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public void deleteEpisode(Long id){
        Episode episode= episodeRepository.findById(id).orElseThrow(()->new CustomException("Episode not found"));
        episode.setStatus(Status.inactive);
        episodeRepository.save(episode);
    }
}
