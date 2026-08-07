package com.microservices.stylecartbackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleTestController {

    @GetMapping("/api/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {

        return "Welcome Admin";
    }

    @GetMapping("/api/customer/test")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerTest() {

        return "Welcome Customer";
    }

    @GetMapping("/api/common/test")
    public String commonTest() {

        return "Welcome Authenticated User";
    }
}