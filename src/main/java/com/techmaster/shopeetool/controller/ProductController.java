package com.techmaster.shopeetool.controller;

import com.techmaster.shopeetool.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }
    @RolesAllowed({"ROLE_EDITOR","ROLE_CUSTOMER"})
    @GetMapping("spending-chart")
    public ResponseEntity<?> filterProductBy(){
        return ResponseEntity.ok(productService.filterByCategory());
    }
}
