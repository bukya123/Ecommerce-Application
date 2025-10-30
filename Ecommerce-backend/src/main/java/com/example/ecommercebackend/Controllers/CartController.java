package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.DTOs.CartItemRequestDTO;
import com.example.ecommercebackend.DTOs.CartRequestDTO;
import com.example.ecommercebackend.DTOs.CartResponseDTO;
import com.example.ecommercebackend.Modules.Cart;
import com.example.ecommercebackend.Modules.CartItem;
import com.example.ecommercebackend.Repositories.CartRepo;
import com.example.ecommercebackend.Services.CartService;
import com.example.ecommercebackend.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepo cartRepo;


    @PostMapping("/cart/create")
    public ResponseEntity<String> addOrUpdateCart(@RequestBody List<CartItemRequestDTO> cartItemRequestDTOS){
        String s=cartService.addOrUpdateCart(cartItemRequestDTOS);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartRequestDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartRequestDTO cartRequestDTO=cartService.addProductToCart(productId,quantity);
        return new  ResponseEntity<>(cartRequestDTO, HttpStatus.OK);
    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartRequestDTO>> getAllCarts(){
        return new ResponseEntity<>(cartService.getAllCarts(),HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartRequestDTO> getUserCart(){
        String emailId=authUtil.loggedInEmail();
        Cart cart=cartRepo.findByEmail(emailId);
        CartRequestDTO cartRequestDTO=cartService.getCart(emailId,cart.getCartId());
        return new ResponseEntity<>(cartRequestDTO,HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartRequestDTO> updateProductInCart(@PathVariable Long productId, @PathVariable String operation) {
        CartRequestDTO cartRequestDTO=cartService.updateProductCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartRequestDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,@PathVariable Long productId) {
        String s=cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


}
