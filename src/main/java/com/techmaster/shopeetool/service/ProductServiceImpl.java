package com.techmaster.shopeetool.service;

import com.techmaster.shopeetool.dto.FilterDTO;
import com.techmaster.shopeetool.dto.ResponseFilterDTO;
import com.techmaster.shopeetool.model.Product;
import com.techmaster.shopeetool.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<FilterDTO> filterByCategory() {
        List<FilterDTO> brandQuantities = new ArrayList<>();

        // Gọi phương thức của repository để truy vấn dữ liệu từ cơ sở dữ liệu
        List<Object[]> result = productRepository.findProductQuantityByBrand();
        for (Object[] row : result) {
            FilterDTO brandQuantity = new FilterDTO();
            brandQuantity.setBrand((String) row[0]);
            brandQuantity.setQuantity(((Long) row[1]).intValue());
            brandQuantities.add(brandQuantity);
        }
        return brandQuantities;

    }

//    @Override
//    public List<Product> filterByCategory() {
//        return productRepository.findAllByCategory();
//    }
}
