package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.DTOs.OrderItemRequestDTO;
import com.example.ecommercebackend.DTOs.OrderRequestDTO;
import com.example.ecommercebackend.DTOs.OrderResponseDTO;
import com.example.ecommercebackend.Modules.*;
import com.example.ecommercebackend.Repositories.*;
import com.example.ecommercebackend.Util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AuthUtil  authUtil;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartServiceImp cartServiceImp;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentRepo paymentRepo;


    @Transactional
    @Override
    public OrderResponseDTO placeOrder(String paymentMethod, OrderRequestDTO orderRequestDTO) {
        String email=authUtil.loggedInEmail();
        Cart cart=cartRepo.findByEmail(email);
        if(cart==null){
            throw new APIException("No cart found");
        }

        Optional<Address> Optaddress=addressRepo.findById(orderRequestDTO.getAddressId());
        if(!Optaddress.isPresent()){
            throw new APIException("Address not found");
        }
        Address address=Optaddress.get();

        Order order=new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("Order is Accepted");
        order.setTotalAmount(cart.getTotalPrice());
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, orderRequestDTO.getPgPaymentId(), orderRequestDTO.getPgStatus(), orderRequestDTO.getPgResponseMessage(), orderRequestDTO.getPgName());
        payment = paymentRepo.save(payment);
        order.setPayment(payment);

        Order savedOrder=orderRepo.save(order);

        List<CartItem> cartItems=cart.getCartItem();
        List<OrderItem> orderItems=new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem=new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getPrice());
            orderItems.add(orderItem);
        }
        orderItemRepo.saveAll(orderItems);


        for(int j=0;j<cartItems.size();j++){
            CartItem item=cartItems.get(j);
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepo.save(product);

            // Remove items from cart
            cartServiceImp.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        }

//

        OrderResponseDTO orderResponseDTO = modelMapper.map(savedOrder, OrderResponseDTO.class);
        orderItems.forEach(item -> orderResponseDTO.getOrderItems().add(modelMapper.map(item, OrderItemRequestDTO.class)));
        orderResponseDTO.setAddressId(orderRequestDTO.getAddressId());

        return orderResponseDTO;

    }
}
