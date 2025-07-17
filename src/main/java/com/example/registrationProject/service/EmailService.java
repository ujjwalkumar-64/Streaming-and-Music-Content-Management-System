package com.example.registrationProject.service;

import com.example.registrationProject.entity.EmailDetail;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMail(EmailDetail emailDetail);
    String sendMailWithAttachment(EmailDetail emailDetail);
}
