package com.example.registrationProject.service;

import com.example.registrationProject.entity.Product;
import com.example.registrationProject.request.ProductRequest;
import com.example.registrationProject.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest);
    ProductResponse updateProduct(ProductRequest productRequest);
    void deleteProduct(Long productId);
    List<Product> productLists();
}
