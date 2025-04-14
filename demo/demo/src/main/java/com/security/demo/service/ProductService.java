package com.security.demo.service;

import com.security.demo.model.Product;
import com.security.demo.payload.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {

   Page<Product> searchProducts(Map<String, String> filters, Pageable pageable);
   ProductDTO createProduct(ProductDTO productDTO);
   ProductDTO deleteProduct(Long productId);
   ProductDTO updateProduct(ProductDTO productDTO,Long productId);

   ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
   List<Product> parseCSVFile(MultipartFile file);
}
