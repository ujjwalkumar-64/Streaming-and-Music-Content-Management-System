package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Album;
import com.example.registrationProject.entity.Genre;
import com.example.registrationProject.entity.Label;
import com.example.registrationProject.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track,Long> {
    @Query("select t from Track  t where t.id=:id")
    Optional<Track> findById(Long id);

    @Query("select  t from  Track t where t.title=:name")
    Optional<Track> findByName(String name);


    Optional<Object> findTracksByGenres(List<Genre> genres);

    Optional<Object> findByLabel(Label label);
}
