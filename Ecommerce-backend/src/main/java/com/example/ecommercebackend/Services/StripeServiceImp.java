package com.example.ecommercebackend.Services;


import com.example.ecommercebackend.DTOs.StripePaymentRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImp implements StripeService {
    @Value("${stripe.secret.key}")
    private String stripeKey;

    @Override
    public PaymentIntent paymentContent(StripePaymentRequestDTO stripePaymentRequestDTO) throws StripeException {
        Stripe.apiKey =stripeKey;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(stripePaymentRequestDTO.getAmount())
                        .setCurrency(stripePaymentRequestDTO.getCurrency())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        return PaymentIntent.create(params);
    }
}
