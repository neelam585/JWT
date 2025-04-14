package com.security.demo.service;

import com.security.demo.exception.ResourceNotFoundException;
import com.security.demo.model.Product;
import com.security.demo.payload.ProductDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSpecification {

    public static Specification<Product> filterByCriteria(Map<String,String> filters) {
        return (root, query, criteriaBuilder) -> {
             List<Predicate> predicates = new ArrayList<>();
             filters.forEach((key,value) -> {
                 if(value != null && !value.isEmpty()) {
                     switch (key) {
                         case "name" -> predicates.add(criteriaBuilder.like(root.get("name"), "%" + value + "%"));
                         case "brand" -> predicates.add(criteriaBuilder.equal(root.get("brand"), value));
                         case "color" -> predicates.add(criteriaBuilder.equal(root.get("color"), value));
                         case "size" -> predicates.add(criteriaBuilder.equal(root.get("size"), value));
                         case "minPrice" -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), Double.valueOf(value)));
                         case "maxPrice" -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), Double.valueOf(value)));
                     }
                 }
             });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
