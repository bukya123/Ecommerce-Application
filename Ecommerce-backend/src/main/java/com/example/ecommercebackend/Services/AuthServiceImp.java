package com.example.ecommercebackend.Services;


import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.DTOs.AppUserRequestDTO;
import com.example.ecommercebackend.DTOs.AppUserResponseDTO;
import com.example.ecommercebackend.Modules.AppRole;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Role;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import com.example.ecommercebackend.Repositories.RoleRepo;
import com.example.ecommercebackend.Security.JWT.JwtUtils;
import com.example.ecommercebackend.Security.JwtService.UserDetailsImp;
import com.example.ecommercebackend.Security.Request.LoginRequest;
import com.example.ecommercebackend.Security.Request.SignUpRequest;
import com.example.ecommercebackend.Security.Response.UserInfoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public UserInfoResponse SignIn(LoginRequest loginRequest) {
        // it will authenticate and gives authentication object
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImp userDetailsImp=(UserDetailsImp)authentication.getPrincipal();

        List<String> roles = userDetailsImp.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfoResponse=new UserInfoResponse();
        userInfoResponse.setId(userDetailsImp.getId());
        userInfoResponse.setUsername(userDetailsImp.getUsername());
        userInfoResponse.setEmail(userDetailsImp.getEmail());
        userInfoResponse.setJwtToken(jwtUtils.generateJwtCookie(userDetailsImp).toString());
        userInfoResponse.setRoles(roles);
        return userInfoResponse;
    }



    @Override
    public AppUser SignUp(SignUpRequest signUpRequest) {
        if (appUserRepo.existsByUsername(signUpRequest.getUsername())) {
             throw new APIException("Error: Username is already taken!");
        }

        if (appUserRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new APIException("Error: Email is already in use!");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        // Create new user's account
        AppUser user = new AppUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        List<String> strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER);
            if (userRole != null) {
                throw new APIException("Error: Role is not found");
            }
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByRoleName(AppRole.ROLE_ADMIN);
                        if(adminRole==null){
                            throw new APIException("Error: Role is not found!");
                        }
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepo.findByRoleName(AppRole.ROLE_SELLER);
                        if(modRole==null){
                            throw new APIException("Error: Role is not found!");
                        }
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER);
                        if(userRole==null){
                            throw new APIException("Error: Role is not found!");
                        }
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        appUserRepo.save(user);
        return user;

    }


    public UserInfoResponse getCurrentUserDetails(Authentication authentication){
        UserInfoResponse userInfoResponse=new UserInfoResponse();
        UserDetailsImp userDetailsImp=(UserDetailsImp)authentication.getPrincipal();
        List<String> roles = userDetailsImp.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        userInfoResponse.setUsername(userDetailsImp.getUsername());
        userInfoResponse.setEmail(userDetailsImp.getEmail());
        userInfoResponse.setRoles(roles);
        return userInfoResponse;

    }
    public ResponseCookie logout(){
        return jwtUtils.getCleanJwtCookie();
    }

    public AppUserResponseDTO getAllSellers(Pageable pageable) {
        Page<AppUser> allUsers = appUserRepo.findByRoleName(AppRole.ROLE_SELLER,pageable);

        List<AppUserRequestDTO> userDtos = allUsers.getContent()
                .stream()
                .map(p -> modelMapper.map(p, AppUserRequestDTO.class))
                .collect(Collectors.toList());

        AppUserResponseDTO response = new AppUserResponseDTO();

        response.setContent(userDtos);
        response.setPageNumber(allUsers.getNumber());
        response.setPageSize(allUsers.getSize());
        response.setTotalElements(allUsers.getTotalElements());
        response.setTotalPages(allUsers.getTotalPages());
        response.setLastPage(allUsers.isLast());
        return response;
    }

}
