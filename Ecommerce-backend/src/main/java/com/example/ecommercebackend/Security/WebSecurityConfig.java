package com.example.ecommercebackend.Security;

import com.example.ecommercebackend.Modules.AppRole;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Role;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import com.example.ecommercebackend.Repositories.RoleRepo;
import com.example.ecommercebackend.Security.JWT.AuthEntryPointJwt;
import com.example.ecommercebackend.Security.JWT.JwtFilter;
import com.example.ecommercebackend.Security.JwtService.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public JwtFilter authenticationJwtFilter() {
        return new JwtFilter();
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/seller/**").permitAll()
                                .requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImp);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



    @Bean
    public CommandLineRunner initData(RoleRepo roleRepository, AppUserRepo userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER);
            if(userRole==null) {
                Role newUserRole = new Role();
                newUserRole.setRoleName(AppRole.ROLE_USER);
                 roleRepository.save(newUserRole);
                 userRole=newUserRole;
            };

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER);
            if(sellerRole==null) {
                Role newSellerRole = new Role();
                newSellerRole.setRoleName(AppRole.ROLE_SELLER);
                 roleRepository.save(newSellerRole);
                 sellerRole=newSellerRole;
            }

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN);
            if(adminRole==null) {
                Role newAdminRole = new Role();
                newAdminRole.setRoleName(AppRole.ROLE_ADMIN);
                 roleRepository.save(newAdminRole);
                 adminRole=newAdminRole;
            }


            List<Role> userRoles=new ArrayList<>();
            List<Role> sellerRoles=new ArrayList<>();
            List<Role> adminRoles=new ArrayList<>();

            userRoles.add(userRole);

            sellerRoles.add(sellerRole);

            adminRoles.add(adminRole);
            adminRoles.add(sellerRole);
            adminRoles.add(userRole);




            // Create users if not already present
            if (!userRepository.existsByUsername("user")) {
                AppUser user1 = new AppUser("user", "user@gmail.com", passwordEncoder.encode("user@123"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller")) {
                AppUser seller1 = new AppUser("seller", "seller@gmail.com", passwordEncoder.encode("seller@123"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                AppUser admin = new AppUser("admin", "admin@gmail.com", passwordEncoder.encode("admin@123"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            AppUser appUser=userRepository.findByUsername("user");
            if(appUser!=null){
                appUser.setRoles(userRoles);
                userRepository.save(appUser);
            }

            AppUser appUser1=userRepository.findByUsername("seller");
            if(appUser1!=null){
                appUser1.setRoles(sellerRoles);
                userRepository.save(appUser1);
            }

            AppUser appUser2=userRepository.findByUsername("admin");
            if(appUser2!=null){
                appUser2.setRoles(adminRoles);
                userRepository.save(appUser2);
            }

        };
    }

}
