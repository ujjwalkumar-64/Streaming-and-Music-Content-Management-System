package com.example.registrationProject.repository;


import com.example.registrationProject.entity.OTP;
import com.example.registrationProject.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP,Long> {

    @Query("select o from OTP o where o.email=:email and o.status=:status")
    Optional<OTP>findByEmailAndStatus(String email, Status status);

    @Query("select o from OTP o where o.email=:email ")
    List<OTP> findByEmail(String email);
}
