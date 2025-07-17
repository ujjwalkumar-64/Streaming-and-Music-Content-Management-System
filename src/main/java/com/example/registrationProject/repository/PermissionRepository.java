package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT  P FROM Permission P WHERE P.permission_name=:name")
    Optional<Permission> findByPermission_name(String name);
}
