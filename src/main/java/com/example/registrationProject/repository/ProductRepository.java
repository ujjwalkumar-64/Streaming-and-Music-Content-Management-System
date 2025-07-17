package com.example.registrationProject.repository;

import com.example.registrationProject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from Product p where p.productName=:productName")
    Optional<Product> findByProductName(String productName);

}
