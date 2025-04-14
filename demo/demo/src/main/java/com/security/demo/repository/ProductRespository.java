package com.security.demo.repository;

import com.security.demo.model.Product;
import com.security.demo.payload.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


public interface ProductRespository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    ProductDTO findBySku(String sku);
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
