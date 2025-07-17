package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.EmailDetail;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}") private String sender;

    @Override
    public void sendSimpleMail(EmailDetail emailDetail) {
        try{
            SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailDetail.getRecipientEmail());
            simpleMailMessage.setSubject(emailDetail.getSubject());
            simpleMailMessage.setText(emailDetail.getMessage());
            javaMailSender.send(simpleMailMessage);

        }
        catch (MailException e){
            throw new CustomException("Error while mail sending: " +e.getMessage());
        }

    }

    @Override
   public  String sendMailWithAttachment(EmailDetail emailDetail) {

        MimeMessage mimeMessage= javaMailSender.createMimeMessage();

        try{
           MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage,true);
           mimeMessageHelper.setFrom(sender);
           mimeMessageHelper.setTo(emailDetail.getRecipientEmail());
           mimeMessageHelper.setSubject(emailDetail.getSubject());
           mimeMessageHelper.setText(emailDetail.getMessage());

           FileSystemResource file= new FileSystemResource(new File(emailDetail.getAttachment()));
           mimeMessageHelper.addAttachment(file.getFilename(), file);

           javaMailSender.send(mimeMessage);
           return "mail send successfully";
        }
        catch (MessagingException e){
            throw new CustomException("Error while mail sending: " +e.getMessage());
        }

   }
}
