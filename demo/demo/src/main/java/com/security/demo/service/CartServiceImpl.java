package com.security.demo.service;

import com.security.demo.exception.APIException;
import com.security.demo.exception.ResourceNotFoundException;
import com.security.demo.model.Cart;
import com.security.demo.model.CartItem;
import com.security.demo.model.Product;
import com.security.demo.model.User;
import com.security.demo.payload.CartDTO;
import com.security.demo.payload.ProductDTO;
import com.security.demo.repository.CartItemRepository;
import com.security.demo.repository.CartRepository;
import com.security.demo.repository.ProductRespository;
import com.security.demo.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRespository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart  = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getName() + " already exists in the cart");
        }

        if (product.getStock() == 0) {
            throw new APIException(product.getName() + " is not available");
        }

        if (product.getStock() < quantity) {
            throw new APIException("Please, make an order of the " + product.getName()
                    + " less than or equal to the quantity " + product.getStock() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
       // newCartItem.setDiscount(2342.00);
        newCartItem.setPrice(product.getPrice());

        cartItemRepository.save(newCartItem);

        product.setStock(product.getStock());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

        List<CartItem> cartItems = cart.getCartItem();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            //map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }



    @Override
    public List<CartDTO> getAllCarts() {
        User user = authUtil.getLoggdInUser();
        Cart cart = cartRepository.findCartByEmail(user.getEmail());
        if(cart == null){
            throw new ResourceNotFoundException("Cart","email",user.getEmail());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> product = cart.getCartItem().stream().map(item -> {

            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);

            return productDTO;
        }).toList(); 

        return (List<CartDTO>) cartDTO;
    }


    private Cart createCart() {
        Cart userCart  = cartRepository.findCartByEmail(authUtil.getLoggedInEmail());
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.getLoggdInUser());
        Cart newCart =  cartRepository.save(cart);

        return newCart;
    }






}
