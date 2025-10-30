package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripePaymentRequestDTO {
    private Long amount;
    private String currency;
}
