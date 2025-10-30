package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long orderId;
    private String email;
    private List<OrderItemRequestDTO> orderItems;
    private LocalDate orderDate;
    private PaymentRequestDTO payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
}
