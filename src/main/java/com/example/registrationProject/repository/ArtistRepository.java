package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {
    @Query("select  a from Artist a where a.user.id=:userId")
    Optional<Artist> findByUserId(Long userId);

}
