package com.security.demo.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long Id;
    @NotBlank(message = "name is required")
    @Size(min = 3, message = "name must be at least 3 characters long")
    private String name;
    private String sku;
    private Float price;
    private String brand;
    private String color;
    private String size;
    private Integer stock;
    private Integer quantity;
    private Integer categoryId;
    private String image;
    private Boolean Status;
}
