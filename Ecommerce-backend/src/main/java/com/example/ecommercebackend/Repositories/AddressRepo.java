package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.Address;
import com.example.ecommercebackend.Modules.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {
    List<Address> findByAppUser(AppUser appUser);
}
