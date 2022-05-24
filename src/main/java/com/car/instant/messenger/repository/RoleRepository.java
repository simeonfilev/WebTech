package com.car.instant.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.instant.messenger.domain.entities.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, String> {
    UserRole getByAuthority(String authority);
}
