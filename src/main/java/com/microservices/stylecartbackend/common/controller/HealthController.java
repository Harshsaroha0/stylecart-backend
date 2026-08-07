package com.microservices.stylecartbackend.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@Tag(name = "Health Check", description = "Application Health APIs")
public class HealthController {

    @Operation(summary = "Application Health")

    @GetMapping("/health")
    public String health() {

        return "StyleCart Backend Running";
    }
}