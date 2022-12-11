package com.easyvaluation.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    UserRole save(UserRole userRole);
    UserRole findByName(String name);
}
