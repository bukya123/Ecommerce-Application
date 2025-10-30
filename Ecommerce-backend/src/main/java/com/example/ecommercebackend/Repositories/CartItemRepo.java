package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    @Modifying
    @Query("delete from CartItem ci where ci.cart.cartId=?1 and ci.product.productId=?2")
     void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId=?1 AND ci.cart.cartId= ?2")
    CartItem findByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Query("delete from CartItem ci where ci.cart.cartId= ?1")
    void deleteCartItemByCartId(Long cartId);

}
