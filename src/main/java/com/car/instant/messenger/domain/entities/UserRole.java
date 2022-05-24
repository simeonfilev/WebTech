package com.car.instant.messenger.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity implements GrantedAuthority {
    private String authority;

    public UserRole() {
    }

    @Override
    @Column(name = "authority", nullable = false)
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }
}