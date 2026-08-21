package com.microservices.stylecartbackend.serviceInterface;

import com.microservices.stylecartbackend.dto.ProductImageRequest;
import com.microservices.stylecartbackend.dto.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse create(ProductImageRequest request);
    ProductImageResponse getById(long id);
    List<ProductImageResponse> getAll();
    ProductImageResponse update(Long id ,ProductImageRequest request);
    void delete(Long id);
}
