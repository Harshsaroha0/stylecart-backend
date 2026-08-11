package com.microservices.stylecartbackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @Size(min = 3 , max = 100)
    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

}
