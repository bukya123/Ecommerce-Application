package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.StripePaymentRequestDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {

    PaymentIntent paymentContent(StripePaymentRequestDTO stripePaymentRequestDTO) throws StripeException;

}
