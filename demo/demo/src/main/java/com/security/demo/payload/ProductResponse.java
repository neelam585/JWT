package com.security.demo.payload;

import java.util.List;

public class ProductResponse {

    private List<ProductDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
