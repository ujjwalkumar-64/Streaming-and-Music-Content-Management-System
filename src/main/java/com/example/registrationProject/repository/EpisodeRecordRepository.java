package com.example.registrationProject.repository;

import com.example.registrationProject.entity.EpisodeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRecordRepository extends JpaRepository<EpisodeRecord,Long> {
}
