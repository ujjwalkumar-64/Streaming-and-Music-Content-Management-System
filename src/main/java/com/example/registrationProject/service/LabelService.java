package com.example.registrationProject.service;

import com.example.registrationProject.request.LabelRequest;
import com.example.registrationProject.response.DTO.LabelDto;
import com.example.registrationProject.response.LabelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LabelService {

    LabelDto updateLabel(LabelRequest labelRequest);
    LabelDto getMyProfile();
    List<LabelDto> getAllLabels();

}
