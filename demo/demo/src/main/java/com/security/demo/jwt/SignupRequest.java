package com.security.demo.jwt;

import java.util.Set;


import lombok.Data;

@Data
public class SignupRequest {

    private String username;


    private String email;

    private Set<String> role;

    private String password;

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}