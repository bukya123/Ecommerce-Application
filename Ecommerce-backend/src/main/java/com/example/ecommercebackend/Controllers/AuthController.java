package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.Configs.AppConstants;
import com.example.ecommercebackend.Modules.AppRole;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Role;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import com.example.ecommercebackend.Repositories.RoleRepo;
import com.example.ecommercebackend.Security.Request.LoginRequest;
import com.example.ecommercebackend.Security.Request.SignUpRequest;
import com.example.ecommercebackend.Security.Response.MessageResponse;
import com.example.ecommercebackend.Security.Response.UserInfoResponse;
import com.example.ecommercebackend.Services.AuthService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    RoleRepo roleRepo;


    @PostMapping("/SignIn")
    public ResponseEntity<UserInfoResponse> SignIn(@RequestBody LoginRequest loginRequest) {
        UserInfoResponse userInfoResponse =authService.SignIn(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userInfoResponse.getJwtToken()).body(userInfoResponse);
    }


    @PostMapping("/SignUp")
    public ResponseEntity<AppUser> SignUp(@RequestBody SignUpRequest signUpRequest) {
        AppUser appUser=authService.SignUp(signUpRequest);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }

    @PostMapping("/SignOut")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = authService.logout();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You are logouted out"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber ,
                Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }

}
