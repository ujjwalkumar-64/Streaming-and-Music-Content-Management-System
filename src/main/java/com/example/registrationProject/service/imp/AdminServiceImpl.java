package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Language;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.GenreRepository;
import com.example.registrationProject.repository.LanguageRepository;
import com.example.registrationProject.response.DTO.GenreDto;
import com.example.registrationProject.response.DTO.LanguageDto;
import com.example.registrationProject.service.AdminService;
import com.example.registrationProject.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    RedisService redisService;

    @Override
    public List<GenreDto> getGenres() throws CustomException {

        String redisKey="genres:list";

        List<GenreDto> cachedResponse= (List<GenreDto>) redisService.get(redisKey);

        if(cachedResponse!=null){
            return cachedResponse;
        }

        List<Genre> genres= genreRepository.findAll();
        if(genres.isEmpty()){
            throw new CustomException("Genre Not Found");
        }
        List<GenreDto> genresDtos= new ArrayList<>();
        genres.forEach(genre->{
            genresDtos.add(new GenreDto().builder()
                    .id(genre.getId())
                    .name(genre.getName())
                    .build());
        });

        redisService.saveWithTTL(redisKey,genresDtos,10, TimeUnit.MINUTES);

        return genresDtos;
    }

    @Override
    public List<LanguageDto> getLanguages() throws  CustomException{
        String redisKey="languages:list";
        List<LanguageDto> cachedResponse= (List<LanguageDto>) redisService.get(redisKey);
        if(cachedResponse!=null){
            return cachedResponse;
        }

        List<Language> languages= languageRepository.findAll();
        if(languages.isEmpty()){
            throw new CustomException("no language found");
        }
        List<LanguageDto> languageDtos= new ArrayList<>();
        languages.forEach(language->{
            languageDtos.add(new LanguageDto().builder()
                            .id(language.getId())
                            .language(language.getName())
                    .build());
        });

        redisService.saveWithTTL(redisKey,languageDtos,10, TimeUnit.MINUTES);

        return languageDtos;
    }
}
