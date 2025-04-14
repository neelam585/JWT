package com.security.demo.controller;

import com.security.demo.model.Cart;
import com.security.demo.model.Product;
import com.security.demo.payload.CartDTO;
import com.security.demo.repository.CartRepository;
import com.security.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/api")
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("addtocart/{productId}/{quantity}")
    public ResponseEntity<CartDTO> addToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO response = cartService.addProductToCart(productId,quantity);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("cart/item")
    public ResponseEntity<List<CartDTO>> getAllCartItem(){
        List<CartDTO> response = cartService.getAllCarts();
        return ResponseEntity.ok(response);
    }



}
