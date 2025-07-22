package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Podcaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcasterRepository extends JpaRepository<Podcaster,Long> {
}
