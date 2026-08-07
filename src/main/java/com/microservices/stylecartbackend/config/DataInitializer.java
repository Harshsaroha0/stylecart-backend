package com.microservices.stylecartbackend.config;

import com.microservices.stylecartbackend.constant.RoleConstants;
import com.microservices.stylecartbackend.entity.Role;
import com.microservices.stylecartbackend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole(
                RoleConstants.ROLE_ADMIN,
                "Administrator with full system access"
        );

        createRole(
                RoleConstants.ROLE_CUSTOMER,
                "Customer with shopping access"
        );
    }

    private void createRole(String roleName, String description) {

        if (roleRepository.existsByName(roleName)) {
            return;
        }

        Role role = new Role();
        role.setName(roleName);
        role.setDescription(description);

        roleRepository.save(role);
    }
}