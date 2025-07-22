package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    @Query("SELECT COUNT(p) > 0 FROM Playlist p JOIN p.tracks t WHERE t.id = :trackId")
    Boolean checkTrackPresentInPlaylist( Long trackId);

    Boolean existsByNameAndCreator_Id(String name, Long creatorId);
}
