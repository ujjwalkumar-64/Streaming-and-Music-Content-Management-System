package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {
    @Query("select  l from Label l where l.ownerUser=:userId")
    Optional<Label> findByUserId(Long userId);
}
