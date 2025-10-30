package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    @Query("select c from Cart c where c.appUser.email=?1")
    Cart findByEmail(String email);
    @Query("select c from Cart c where c.appUser.email=?1 and c.cartId=?2")
    Cart findByEmailAndCartId(String email,Long cartId);

    @Query("select c from Cart c JOIN FETCH c.cartItem ci JOIN FETCH ci.product p WHERE p.productId = ?1 ")
    List<Cart> findByProductId(Long productId);

    Cart findByAppUser(AppUser appUser);


}
