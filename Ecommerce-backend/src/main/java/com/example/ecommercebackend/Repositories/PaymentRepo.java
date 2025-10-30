package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
