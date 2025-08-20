package com.example.registrationProject.repository;

import com.example.registrationProject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track,Long>, TrackRepositoryCustom {
    @Query("select t from Track  t where t.id=:id")
    Optional<Track> findById(Long id);

    @Query("select  t from  Track t where t.title=:name")
    Optional<Track> findByName(String name);


    Optional<Object> findTracksByGenres(List<Genre> genres);

    Optional<Object> findByLabel(Label label);


}
