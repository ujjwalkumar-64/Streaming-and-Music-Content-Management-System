package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.ProductRequest;
import com.example.registrationProject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "/product/create")
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest){
        try{

        return ResponseEntity.ok(productService.addProduct(productRequest));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping(value = "/product/update")
    public ResponseEntity<Object> updateProduct(@RequestBody ProductRequest productRequest){
        try{
            return ResponseEntity.ok(productService.updateProduct(productRequest));
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/product/all")
    public ResponseEntity<Object> getAllProducts(){
        try{
            return ResponseEntity.ok(productService.productLists());
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping(value = "product/delete")
    public ResponseEntity<Object> deleteProduct(@RequestParam(value = "productId") Long productId){
        try{
            productService.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
