package org.zerock.mallapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;
import org.zerock.mallapi.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    // 장바구니 수량 변경 혹은 삭제 요청 (수량 0 이하 시 삭제 처리)
    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO) {
        log.info("changeCart called with: " + itemDTO);

        if (itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);
    }

    // 로그인한 사용자의 장바구니 아이템 목록 조회
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {
        String email = principal.getName();
        log.info("getCartItems called for email: " + email);
        return cartService.getCartItems(email);
    }

    // 장바구니 아이템 삭제 요청
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("removeFromCart called with cino: " + cino);
        return cartService.remove(cino);
    }
}
