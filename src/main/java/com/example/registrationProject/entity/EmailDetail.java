package com.example.registrationProject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetail {
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private String attachment;

}
