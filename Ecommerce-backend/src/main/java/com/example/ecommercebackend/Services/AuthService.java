package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.AppUserResponseDTO;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Security.Request.LoginRequest;
import com.example.ecommercebackend.Security.Request.SignUpRequest;
import com.example.ecommercebackend.Security.Response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface AuthService {

    public UserInfoResponse SignIn(LoginRequest loginRequest);
    public AppUser SignUp(SignUpRequest signUpRequest);
    public UserInfoResponse getCurrentUserDetails(Authentication authentication);
    public ResponseCookie logout();

    AppUserResponseDTO getAllSellers(Pageable pageDetails);

}
