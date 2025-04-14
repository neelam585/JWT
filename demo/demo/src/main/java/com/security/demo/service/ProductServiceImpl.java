package com.security.demo.service;

import com.opencsv.CSVReader;
import com.security.demo.exception.ResourceNotFoundException;
import com.security.demo.model.Product;
import com.security.demo.payload.ProductDTO;
import com.security.demo.repository.ProductRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
   // @Cacheable(value = "products", key = "#filters.toString().concat('-').concat(#pageable.toString())")
    public Page<Product> searchProducts(Map<String, String> filters, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.filterByCriteria(filters);
        return productRespository.findAll(spec, pageable);
    }


    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        Product saveProduct = productRespository.save(product);
        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productDb = productRespository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id:"+productId));
        productRespository.delete(productDb);
        return modelMapper.map(productDb, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        ProductDTO ProductDB = productRespository.findBySku(productDTO.getSku());
        if(ProductDB != null){
            throw new RuntimeException("Product already exists with sku:"+productDTO.getSku());
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product.setId(productId);
        Product saveProduct = productRespository.save(product);
        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRespository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (image.isEmpty()) {
            new RuntimeException("Please select a file to upload.");
        }

        try {
            // Ensure directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file
            String filePath = UPLOAD_DIR + image.getOriginalFilename();
            image.transferTo(new File(filePath));
            productFromDb.setImage(image.getOriginalFilename());

        } catch (IOException e) {
            System.out.println(e.getMessage());
            new RuntimeException("Error uploading image: " + e.getMessage());
        }
        Product updatedProduct = productRespository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public List<Product> parseCSVFile(MultipartFile file) {
        List<Product> products = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] values;
            boolean firstRow = true; // To skip header row
            while ((values = csvReader.readNext()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                String name = values[0];
                String sku = values[1];
                String brand = values[2];
                String color = values[3];
                Product productdto = new Product();
                productdto.setName(name);
                productdto.setBrand(brand);
                productdto.setBrand(color);
                productdto.setSku(sku);
//                Product productsave = productRespository.save();
                products.add(productdto);
            }
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }

        return products;
    }
}
