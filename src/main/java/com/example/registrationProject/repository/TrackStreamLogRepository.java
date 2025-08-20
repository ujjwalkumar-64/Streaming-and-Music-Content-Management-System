package com.example.registrationProject.repository;

import com.example.registrationProject.entity.TrackStreamLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackStreamLogRepository  extends JpaRepository<TrackStreamLog,Long> {

}
