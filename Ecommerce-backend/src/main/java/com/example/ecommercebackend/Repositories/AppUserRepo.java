package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.AppRole;
import com.example.ecommercebackend.Modules.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.roleName = :role")
    Page<AppUser> findByRoleName(@Param("role") AppRole role, Pageable pageable);

}
