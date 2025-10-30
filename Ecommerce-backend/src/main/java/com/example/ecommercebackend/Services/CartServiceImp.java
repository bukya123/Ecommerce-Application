package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.ControllerAdivce.ResourceNotFoundException;
import com.example.ecommercebackend.DTOs.CartItemRequestDTO;
import com.example.ecommercebackend.DTOs.CartRequestDTO;
import com.example.ecommercebackend.DTOs.CartResponseDTO;
import com.example.ecommercebackend.DTOs.ProductRequestDTO;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Cart;
import com.example.ecommercebackend.Modules.CartItem;
import com.example.ecommercebackend.Modules.Product;
import com.example.ecommercebackend.Repositories.CartItemRepo;
import com.example.ecommercebackend.Repositories.CartRepo;
import com.example.ecommercebackend.Repositories.ProductRepo;
import com.example.ecommercebackend.Util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;





    public Cart createCart() {
        Cart cart=cartRepo.findByEmail(authUtil.loggedInEmail());
        if(cart!=null) {
            return cart;
        }
        Cart cart1=new Cart();
        cart1.setTotalPrice(0.0);
        cart1.setAppUser(authUtil.loggedInUser());
        Cart cart2= cartRepo.save(cart1);
        return cart2;
    }

    @Override
    public CartRequestDTO addProductToCart(Long ProductId, Integer Quantity) {

        Cart cart=createCart();

        Optional<Product> Optproduct=productRepo.findById(ProductId);
        if(!Optproduct.isPresent()){
            throw new APIException("Product doesn't exist");
        }
        Product product=Optproduct.get();

        CartItem cartItem=cartItemRepo.findByProductIdAndCartId(ProductId, cart.getCartId());
        if(cartItem!=null){
            throw new APIException("Product already exists in Cart");
        }
        if(product.getQuantity()==0){
            throw new APIException("Product out of stock");
        }
        if(product.getQuantity()<Quantity){
            throw new APIException("Product quantity less than quantity.Please select less quantity");
        }

        CartItem cartItem1=new CartItem();
        cartItem1.setProduct(product);
        cartItem1.setQuantity(Quantity);
        cartItem1.setCart(cart);
        cartItem1.setPrice(product.getSpecialPrice());
        cartItem1.setDiscount(product.getDiscount());
        cartItemRepo.save(cartItem1);
        //Here we are not reducing product quantity _ we will reduce in checkout
        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*Quantity));
        cartRepo.save(cart);

        CartRequestDTO cartRequestDTO=modelMapper.map(cart,CartRequestDTO.class);
        List<CartItem> cartItemList=cart.getCartItem();
        List<ProductRequestDTO> productRequestDTOS=new ArrayList<>();
        for(int j=0;j<cartItemList.size();j++){
            Product product1=cartItemList.get(j).getProduct();
            ProductRequestDTO productRequestDTO=modelMapper.map(product1,ProductRequestDTO.class);
            productRequestDTO.setQuantity(cartItemList.get(j).getQuantity());
            productRequestDTOS.add(productRequestDTO);
        }

        cartRequestDTO.setProducts(productRequestDTOS);
        return cartRequestDTO;
    }

    @Override
    public List<CartRequestDTO> getAllCarts() {
        List<Cart> cartList=cartRepo.findAll();
        if(cartList.isEmpty()){
            throw new APIException("Cart is empty");
        }
        List<CartRequestDTO> cartRequestDTOS=new ArrayList<>();
        for(int j=0;j<cartList.size();j++){
            Cart cart=cartList.get(j);
            CartRequestDTO cartRequestDTO=modelMapper.map(cart,CartRequestDTO.class);
            List<CartItem> cartItemList=cart.getCartItem();
            List<ProductRequestDTO> productRequestDTOS=new ArrayList<>();
            cartItemList.forEach(cartItem->{
                ProductRequestDTO productRequestDTO=modelMapper.map(cartItem.getProduct(),ProductRequestDTO.class);
                productRequestDTO.setQuantity(cartItem.getQuantity());
                productRequestDTOS.add(productRequestDTO);
            });
            cartRequestDTO.setProducts(productRequestDTOS);
            cartRequestDTOS.add(cartRequestDTO);
        }

        return cartRequestDTOS;
    }

    @Override
    public CartRequestDTO getCart(String emailId,Long id){
        Cart cart=cartRepo.findByEmailAndCartId(emailId,id);
        if(cart==null){
            throw new APIException("Cart is empty");
        }
        CartRequestDTO cartRequestDTO=modelMapper.map(cart,CartRequestDTO.class);
        List<CartItem> cartItemList=cart.getCartItem();
        List<ProductRequestDTO> productRequestDTOS=new ArrayList<>();
        for(int j=0;j<cartItemList.size();j++){
            Product product1=cartItemList.get(j).getProduct();
            product1.setQuantity(cartItemList.get(j).getQuantity());
            ProductRequestDTO productRequestDTO=modelMapper.map(product1,ProductRequestDTO.class);
            productRequestDTOS.add(productRequestDTO);
        }
        cartRequestDTO.setProducts(productRequestDTOS);
        return cartRequestDTO;
    }

    @Transactional
    @Override
    public CartRequestDTO updateProductCart(Long ProductId, Integer Quantity){
        String emailId=authUtil.loggedInEmail();
        Cart cart=cartRepo.findByEmail(emailId);
        if(cart==null){
            throw new APIException("Cart is empty");
        }
        CartItem cartItem=cartItemRepo.findByProductIdAndCartId(ProductId,cart.getCartId());
        if(cartItem==null){
            throw new APIException("No Items in cart");
        }
        Product product=cartItem.getProduct();
        if(product.getQuantity()==0){
            throw new APIException("Product out of stock");
        }
        if(product.getQuantity()<Quantity){
            throw new APIException("Product out of stock");
        }

        int newQuantity = cartItem.getQuantity() + Quantity;

        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        if (newQuantity == 0){
            String s=deleteProductFromCart(cart.getCartId(), product.getProductId());
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+Quantity);
            cartItem.setPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getPrice()*Quantity));
            cartRepo.save(cart);
        }

        CartItem updatedCartItem=cartItemRepo.save(cartItem);
        if(updatedCartItem.getQuantity()==0){
            cartItemRepo.deleteById(updatedCartItem.getCartItemId());
        }

        CartRequestDTO cartRequestDTO = modelMapper.map(cart, CartRequestDTO.class);

        List<CartItem> cartItems = cart.getCartItem();

        Stream<ProductRequestDTO> productStream = cartItems.stream().map(item -> {
            ProductRequestDTO prd = modelMapper.map(item.getProduct(), ProductRequestDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });

        cartRequestDTO.setProducts(productStream.toList());
        return cartRequestDTO;

    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long CartId, Long ProductId) {
        Optional<Cart> cart=cartRepo.findById(CartId);
        if(!cart.isPresent()){
            throw new APIException("Cart is empty");
        }
       CartItem cartItem=cartItemRepo.findByProductIdAndCartId(ProductId,CartId);
        if(cartItem==null){
            throw new APIException("No Items in cart");
        }
        cart.get().setTotalPrice(cart.get().getTotalPrice()-(cartItem.getPrice()*cartItem.getQuantity()));
        cartItemRepo.deleteCartItemByCartIdAndProductId(CartId,ProductId);
        return "Product has been deleted";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getPrice() * cartItem.getQuantity());

        cartItem.setPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepo.save(cartItem);
    }

    @Transactional
    @Override
    public String addOrUpdateCart(List<CartItemRequestDTO> cartItemRequestDTOS) {
        //first we need to check whether there is existing cartItems in the cart ,if present delete all
        // of them
        String mail=authUtil.loggedInEmail();
        Cart cart=cartRepo.findByEmail(mail);
        if(cart!=null){
            cartItemRepo.deleteCartItemByCartId(cart.getCartId());
        }else{
            cart = new Cart();
            cart.setTotalPrice(0.00);
            cart.setAppUser(authUtil.loggedInUser());
            cart = cartRepo.save(cart);
        }

        double totalPrice=0.0;

        for(CartItemRequestDTO cartItemRequestDTO:cartItemRequestDTOS){
            Long productId = cartItemRequestDTO.getProductId();
            Integer quantity = cartItemRequestDTO.getQuantity();

            // Find the product by ID
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

            // Directly update product stock and total price
            // product.setQuantity(product.getQuantity() - quantity);
            totalPrice += product.getSpecialPrice() * quantity;

            // Create and save cart item
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepo.save(cartItem);
        }
        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
        return "Cart created/updated with the new items successfully";
    }
}
