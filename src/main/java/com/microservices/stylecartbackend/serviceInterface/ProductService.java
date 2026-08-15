package com.microservices.stylecartbackend.serviceInterface;

import com.microservices.stylecartbackend.dto.ProductRequest;
import com.microservices.stylecartbackend.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    ProductResponse update(Long id , ProductRequest request);

    void delete(Long id);

}
