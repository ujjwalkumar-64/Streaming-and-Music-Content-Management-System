package com.example.registrationProject.repository;

import com.example.registrationProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

}
