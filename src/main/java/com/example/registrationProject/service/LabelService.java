package com.example.registrationProject.service;

import com.example.registrationProject.request.LabelRequest;
import com.example.registrationProject.response.LabelResponse;
import org.springframework.stereotype.Service;

@Service
public interface LabelService {

    LabelResponse updateLabel(LabelRequest labelRequest);

}
