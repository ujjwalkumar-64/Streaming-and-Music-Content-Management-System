package com.example.registrationProject.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.registrationProject.entity.EpisodeRecord;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.TrackRecord;
import com.example.registrationProject.repository.EpisodeRecordRepository;
import com.example.registrationProject.repository.TrackRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService
{

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    TrackRecordRepository trackRecordRepository;

    @Autowired
    EpisodeRecordRepository episodeRecordRepository;

    public Map uploadFile(MultipartFile file, String resourceType) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", resourceType // "image" for photos, "video" for audio
        ));
    }

    public TrackRecord uploadTrackFile(MultipartFile file) throws IOException {

        Map response =   uploadFile(file,"video");

        TrackRecord trackRecord = new TrackRecord();
        trackRecord.setSize(((Number) response.get("bytes")).longValue());
        trackRecord.setDuration(((Number) response.get("duration")).longValue());
        trackRecord.setStatus(Status.active);
        trackRecord.setPath((String) response.get("url"));
        trackRecord.setName((String) response.get("display_name"));
        trackRecord.setType((String) response.get("format"));
        trackRecord.setMetaData((String) response.get("playback_url"));
       return  trackRecordRepository.save(trackRecord);




    }

    public EpisodeRecord uploadEpisodeFile(MultipartFile file) throws IOException {
        Map response =   uploadFile(file,"video");
        EpisodeRecord episodeRecord = new EpisodeRecord();
        episodeRecord.setSize(((Number) response.get("bytes")).longValue());
        episodeRecord.setDuration(((Number) response.get("duration")).longValue());
        episodeRecord.setStatus(Status.active);
        episodeRecord.setPath((String) response.get("url"));
        episodeRecord.setName((String) response.get("display_name"));
        episodeRecord.setType((String) response.get("format"));
        episodeRecord.setMetaData((String) response.get("playback_url"));

        return episodeRecordRepository.save(episodeRecord);

    }

    public String uploadPhoto(MultipartFile file) throws IOException {
        Map response =   uploadFile(file,"image");

        return (String) response.get("url");
    }
}
