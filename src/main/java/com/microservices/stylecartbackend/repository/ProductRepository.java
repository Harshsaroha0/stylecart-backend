package com.microservices.stylecartbackend.repository;

import com.microservices.stylecartbackend.entity.Product;
import com.microservices.stylecartbackend.specification.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {


    boolean existsByName(String name);

}