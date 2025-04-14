package com.security.demo.controller;

import com.security.demo.config.AppConstants;
import com.security.demo.model.Product;
import com.security.demo.payload.CategoryDTO;
import com.security.demo.payload.CategoryResponse;
import com.security.demo.payload.ProductDTO;
import com.security.demo.service.CategoryService;
import com.security.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";



    @GetMapping("/public/product/search")
    public Page<Product> searchProducts(
            @RequestParam Map<String, String> filters,
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return (Page<Product>) productService.searchProducts(filters, pageable);
    }

    @PostMapping("/public/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestPart("product") ProductDTO productDTO,
                                                    @RequestPart("image") MultipartFile image) {


                try {
            //  Ensure productDTO is not null
            if (productDTO == null) {
                productDTO = new ProductDTO();
            }

            //  Process the uploaded image
            if (image != null && !image.isEmpty()) {
                try {
                    // Define upload directory
                 //   String UPLOAD_DIR = "C:/uploads/";
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    // Save file with a unique name
                    String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    String filePath = UPLOAD_DIR + fileName;
                    image.transferTo(new File(filePath));

                    //  Save filename in DTO (not the file object)
                    productDTO.setImage(fileName);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // If no image is uploaded, set a default value
                productDTO.setImage(null);
            }

            //  Save product in DB
            ProductDTO savedProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

        } catch (Exception e) {
                    System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       // return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }


    @DeleteMapping("/public/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
    }

    @PutMapping("/public/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,@Valid @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @PutMapping("/public/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PostMapping("/admin/uploadcsv")
    public ResponseEntity<List<Product>> uploadCSV(@RequestParam("file") MultipartFile file) {


        List<Product> products = productService.parseCSVFile(file);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


}
