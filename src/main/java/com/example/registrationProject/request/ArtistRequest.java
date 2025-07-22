package com.example.registrationProject.request;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArtistRequest {
    private Long id;
    private String artistName;
    private String bio;
    private MultipartFile profilePic;
    private User user;
    private List<Long> genreIds;
    private Status status;
}
