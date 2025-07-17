package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.Product;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.ProductRepository;
import com.example.registrationProject.request.ProductRequest;
import com.example.registrationProject.response.ProductResponse;
import com.example.registrationProject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        try{
        Product savedProduct=productRepository.save(product);
            return new ProductResponse(
                    savedProduct.getId(),
                    savedProduct.getProductName(),
                    savedProduct.getDescription()
            );

        }
        catch(Exception e){
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        Product product= productRepository.findByProductName(productRequest.getProductName()).orElseThrow(()-> new CustomException("Product not found"));
        if(product.getDescription().isEmpty()){
            throw new CustomException("Product description is empty");
        }
        product.setDescription(productRequest.getDescription() );
        Product savedProduct=productRepository.save(product);
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getProductName(),
                savedProduct.getDescription()
        );
    }

    @Override
    public List<Product> productLists(){
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product= productRepository.findById(productId).orElseThrow(()-> new CustomException("Product not found"));
        productRepository.delete(product);
    }

}
