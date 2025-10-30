package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.DTOs.OrderRequestDTO;
import com.example.ecommercebackend.DTOs.OrderResponseDTO;
import com.example.ecommercebackend.DTOs.StripePaymentRequestDTO;
import com.example.ecommercebackend.Services.OrderService;
import com.example.ecommercebackend.Services.OrderServiceImp;
import com.example.ecommercebackend.Services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StripeService stripeService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderResponseDTO> placeOrder(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO =orderService.placeOrder(paymentMethod,orderRequestDTO);
        return new  ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/order/stripe-client-secret")
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentRequestDTO stripePaymentRequestDTO) throws StripeException {
        PaymentIntent paymentContent=stripeService.paymentContent(stripePaymentRequestDTO);
        return new ResponseEntity<>(paymentContent.getClientSecret(), HttpStatus.OK);
    }
}
