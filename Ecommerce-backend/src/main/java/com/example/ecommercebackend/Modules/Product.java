package com.example.ecommercebackend.Modules;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 2,message = "ProductName should be atleast 2 characters")
    private String productName;

    @NotBlank
    @Size(min = 5,message = "ProductDescription should be atleast 2 characters")
    private String productDescription;

    private Double price;
    private Double discount;
    private String image;
    private Double specialPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CartItem> cartItems;

}
