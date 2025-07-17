package com.example.registrationProject.repository;

import com.example.registrationProject.entity.TempUser;
import com.example.registrationProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser,Long> {
    @Query("select u from TempUser u where u.email=:email")
    Optional<TempUser> findByEmail(String email);

    void deleteByEmail(String email);
}
