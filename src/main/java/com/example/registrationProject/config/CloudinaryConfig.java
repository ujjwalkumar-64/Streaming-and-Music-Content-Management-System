package com.example.registrationProject.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class CloudinaryConfig {

    private final Dotenv dotenv;

    public CloudinaryConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @Bean
    public Cloudinary cloudinary() {
        Dotenv dotenv = Dotenv.load();

         return new Cloudinary(
                 ObjectUtils.asMap(
                         "cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
                         "api_key", dotenv.get("CLOUDINARY_API_KEY"),
                         "api_secret", dotenv.get("CLOUDINARY_API_SECRET")

                 )
         );
    }
}

