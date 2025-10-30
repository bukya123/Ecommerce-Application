package com.example.ecommercebackend.Security.JwtService;

import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=appUserRepo.findByUsername(username);
        if(appUser==null){
            throw new UsernameNotFoundException(username);
        }
        return UserDetailsImp.build(appUser);
    }
}
