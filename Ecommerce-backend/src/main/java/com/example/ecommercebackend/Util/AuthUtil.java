package com.example.ecommercebackend.Util;

import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    @Autowired
    AppUserRepo appUserRepo;
    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName());
        if(user == null){
            throw new UsernameNotFoundException("User Not Found with username: " + authentication.getName());
        }
        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName());
        if(user == null){
            throw new UsernameNotFoundException("User Not Found with username: " + authentication.getName());
        }
        return user.getUserId();
    }

    public AppUser loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = appUserRepo.findByUsername(authentication.getName());
        if(user == null){
            throw new UsernameNotFoundException("User Not Found with username: " + authentication.getName());
        }
        return user;
    }
}
