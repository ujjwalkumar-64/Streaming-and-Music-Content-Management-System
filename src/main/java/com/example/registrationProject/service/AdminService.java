package com.example.registrationProject.service;

import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    List<GenreDto> getGenres();

    List<LanguageDto> getLanguages();
}
