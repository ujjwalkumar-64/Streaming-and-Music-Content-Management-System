package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("select r from Role r where r.role=:role")
    Role findbyRole(String role);
}
