package com.microservices.stylecartbackend.serviceInterface;

import com.microservices.stylecartbackend.dto.ProductVariantRequest;
import com.microservices.stylecartbackend.dto.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponse create(ProductVariantRequest request);
    ProductVariantResponse getById(Long id);
    ProductVariantResponse update(Long id ,ProductVariantRequest request);
    List<ProductVariantResponse> getAll();
    void delete(Long id);
}
