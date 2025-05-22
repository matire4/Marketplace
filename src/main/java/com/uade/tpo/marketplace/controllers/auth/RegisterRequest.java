package com.uade.tpo.marketplace.controllers.auth;

import com.uade.tpo.marketplace.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name; // can be firstname or name depending on the user type
    private String lastname; // is not null for user
    private String email; 
    private String cuil; // is not null for gestor
    private String password;
    private Role role; 
    private String phone;
}
