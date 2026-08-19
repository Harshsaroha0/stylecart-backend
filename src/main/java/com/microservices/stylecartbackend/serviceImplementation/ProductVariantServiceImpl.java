package com.microservices.stylecartbackend.serviceImplementation;

import com.microservices.stylecartbackend.dto.ProductVariantRequest;
import com.microservices.stylecartbackend.dto.ProductVariantResponse;
import com.microservices.stylecartbackend.entity.Product;
import com.microservices.stylecartbackend.entity.ProductVariant;
import com.microservices.stylecartbackend.exception.NameAlreadyExistsException;
import com.microservices.stylecartbackend.exception.ResourceNotFoundException;
import com.microservices.stylecartbackend.repository.ProductRepository;
import com.microservices.stylecartbackend.repository.ProductVariantRepository;
import com.microservices.stylecartbackend.serviceInterface.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductVariantServiceImpl  implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;


    @Override
    public ProductVariantResponse create(ProductVariantRequest request) {

        //check if already present
        if (productVariantRepository
                .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCase(
                        request.getProductId(),
                        request.getSize(),
                        request.getColor())) {

            throw new NameAlreadyExistsException(
                    request.getSize() + " - " + request.getColor()
            );
        }

        //find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product",
                                "id",
                                request.getProductId()
                        ));

        ProductVariant productVariant = new ProductVariant();

        productVariant.setProduct(product);
        productVariant.setColor(request.getColor());
        productVariant.setPrice(request.getPrice());
        productVariant.setSize(request.getSize());
        productVariant.setStock(request.getStock());


        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);

        return ProductVariantResponse.builder()
                .id(savedProductVariant.getId())
                .productId(savedProductVariant.getProduct().getId())
                .productName(savedProductVariant.getProduct().getName())
                .size(savedProductVariant.getSize())
                .color(savedProductVariant.getColor())
                .price(savedProductVariant.getPrice())
                .stock(savedProductVariant.getStock())
                .build();
    }

    @Override
    public ProductVariantResponse getById(Long id) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product variant not found "+ id));

        return ProductVariantResponse.builder()
                .id(productVariant.getId())
                .productId(productVariant.getProduct().getId())
                .productName(productVariant.getProduct().getName())
                .size(productVariant.getSize())
                .color(productVariant.getColor())
                .price(productVariant.getPrice())
                .stock(productVariant.getStock())
                .build();
    }

    @Override
    public ProductVariantResponse update(Long id, ProductVariantRequest request) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ProductVariant",
                                "id",
                                id
                        ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product",
                                "id",
                                request.getProductId()
                        ));

        if (productVariantRepository
                .existsByProductIdAndSizeIgnoreCaseAndColorIgnoreCaseAndIdNot(
                        request.getProductId(),
                        request.getSize(),
                        request.getColor(),
                        id)) {
            throw new NameAlreadyExistsException(
                    request.getSize() + " - " + request.getColor()
            );
        }

        productVariant.setProduct(product);
        productVariant.setSize(request.getSize());
        productVariant.setColor(request.getColor());
        productVariant.setPrice(request.getPrice());
        productVariant.setStock(request.getStock());

        ProductVariant updatedProductVariant =
                productVariantRepository.save(productVariant);

        return new ProductVariantResponse(
                updatedProductVariant.getId(),
                updatedProductVariant.getProduct().getName(),
                updatedProductVariant.getProduct().getId(),
                updatedProductVariant.getSize(),
                updatedProductVariant.getColor(),
                updatedProductVariant.getPrice(),
                updatedProductVariant.getStock()
        );
    }


    @Override
    public List<ProductVariantResponse> getAll() {

        List<ProductVariant> productVariantList = productVariantRepository.findAll();

        return productVariantList.stream()
                .map(productVariant -> new ProductVariantResponse(
                        productVariant.getId(),
                        productVariant.getProduct().getName(),
                        productVariant.getProduct().getId(),
                        productVariant.getSize(),
                        productVariant.getColor(),
                        productVariant.getPrice(),
                        productVariant.getStock()
                ))
                .toList();
    }

    @Override
    public void delete(Long id) {

        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product variant not found" + id));

        productVariantRepository.delete(productVariant);

    }
}
