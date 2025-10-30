package com.example.ecommercebackend.Modules;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
    private List<CartItem> cartItem=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser  appUser;

    private double totalPrice=0.0;
}
