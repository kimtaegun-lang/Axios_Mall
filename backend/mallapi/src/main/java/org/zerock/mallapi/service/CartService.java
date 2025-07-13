package org.zerock.mallapi.service;

import java.util.List;

import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface CartService {

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    List<CartItemListDTO> getCartItems(String email);
    List<CartItemListDTO> remove(Long cino);

}
