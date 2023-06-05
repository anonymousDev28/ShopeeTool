package com.techmaster.shopeetool.repository;

import com.techmaster.shopeetool.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p.brand AS brand, SUM(p.quantity) AS quantity FROM Product p GROUP BY p.brand")
    List<Object[]> findProductQuantityByBrand();
}