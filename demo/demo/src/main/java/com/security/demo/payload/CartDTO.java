package com.security.demo.payload;

import com.security.demo.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private List<ProductDTO> products = new ArrayList<>();
    private double totalPrice;
    private Long cartId;
}
