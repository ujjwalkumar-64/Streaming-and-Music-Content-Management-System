package com.example.registrationProject.repository;


import com.example.registrationProject.entity.TrackRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrackRecordRepository extends JpaRepository<TrackRecord, Long> {
}
