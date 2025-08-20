package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.Track;
import com.example.registrationProject.entity.TrackLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackLikeRepository extends JpaRepository<TrackLike, Long> {

    @Query ("select t from TrackLike t where t.track.id= :trackId and t.user.id= :userId ")
    Optional<TrackLike> findByTrackIdAndUserId(Long trackId,Long userId)   ;

    @Query("select case when count(t)>0 then true else false end from TrackLike t where t.user.id=:userId and t.track.id=:trackId and t.status=:status")
    boolean existsByTrackIdAndUserIdAndStatus(Long trackId,Long userId, Status status);

    @Query("select t.track from TrackLike  t where t.user.id=:userId and t.status=com.example.registrationProject.entity.Status.active" )
    List<Track> findLikedTracksByUser(Long userId);

    @Query("select count(t) from TrackLike  t where t.user.id =:userId and t.status=com.example.registrationProject.entity.Status.active")
    Integer countLikedTracksByUserId(Long userId);

    @Query("select count(t) from TrackLike t where t.track.id=: trackId and t.status=com.example.registrationProject.entity.Status.active")
    Integer countLikedTracksByTrackIdId(Long trackId);
}
