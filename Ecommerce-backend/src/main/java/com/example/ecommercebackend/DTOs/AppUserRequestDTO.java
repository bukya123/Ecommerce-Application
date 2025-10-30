package com.example.ecommercebackend.DTOs;

import com.example.ecommercebackend.Modules.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppUserRequestDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private List<Role> roles=new ArrayList<>();
    private AddressRequestDTO address;
    private CartRequestDTO cart;
}
