package com.techmaster.shopeetool.service;

import com.techmaster.shopeetool.dto.FilterDTO;
import com.techmaster.shopeetool.dto.ResponseFilterDTO;
import com.techmaster.shopeetool.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    List<FilterDTO> filterByCategory();
}
