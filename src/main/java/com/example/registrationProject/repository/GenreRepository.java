package com.example.registrationProject.repository;


import com.example.registrationProject.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

}
