package com.microservices.stylecartbackend.serviceInterface;

import com.microservices.stylecartbackend.dto.CategoryRequest;
import com.microservices.stylecartbackend.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    CategoryResponse update ( Long id ,CategoryRequest request);

    void delete(Long id);
}
