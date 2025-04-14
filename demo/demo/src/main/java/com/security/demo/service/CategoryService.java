package com.security.demo.service;

import com.security.demo.payload.CategoryDTO;
import com.security.demo.payload.CategoryResponse;

public interface CategoryService {

     CategoryResponse getAllCategory(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);
     CategoryDTO createCategory(CategoryDTO categoryDTO);
     CategoryDTO deleteCategory(Long categoryId);
     CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);

}
