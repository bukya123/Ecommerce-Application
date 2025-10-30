package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.OrderRequestDTO;
import com.example.ecommercebackend.DTOs.OrderResponseDTO;

public interface OrderService {
    public OrderResponseDTO placeOrder(String paymentMethod,OrderRequestDTO orderRequestDTO);
}
