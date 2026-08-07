package com.microservices.stylecartbackend.repository;


import com.microservices.stylecartbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

}