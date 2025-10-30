package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.AppRole;
import com.example.ecommercebackend.Modules.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Role findByRoleName(AppRole appRole);
}
