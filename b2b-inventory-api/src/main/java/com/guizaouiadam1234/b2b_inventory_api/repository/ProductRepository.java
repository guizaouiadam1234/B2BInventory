package com.guizaouiadam1234.b2b_inventory_api.repository;

import com.guizaouiadam1234.b2b_inventory_api.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockQuantityLessThanEqual(int stockQuantity);
}
