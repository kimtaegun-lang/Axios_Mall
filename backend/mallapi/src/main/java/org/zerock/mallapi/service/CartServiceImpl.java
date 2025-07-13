package org.zerock.mallapi.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;
import org.zerock.mallapi.repository.CartItemRepository;
import org.zerock.mallapi.repository.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();

        // 1. cino가 있으면: 기존 장바구니 아이템 수량 변경
        if (cino != null) {
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();

            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            // 변경 후 현재 회원 장바구니 아이템 목록 반환
            return getCartItems(email);
        }

        // 2. cino가 없으면: 신규 장바구니 아이템 추가
        Cart cart = getCart(email);
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem == null) {
            // 신규 상품 엔티티 생성 (pno만 세팅)
            Product product = Product.builder()
                    .pno(pno)
                    .build();

            // 신규 장바구니 아이템 생성
            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .qty(qty)
                    .build();
        } else {
            // 기존 상품 수량 변경
            cartItem.changeQty(qty);
        }

        // 신규 생성 또는 수량 변경 후 저장
        cartItemRepository.save(cartItem);

        // 변경된 장바구니 아이템 목록 반환
        return getCartItems(email);
    }

    // 사용자의 장바구니가 없으면 새로 생성 후 반환
    private Cart getCart(String email) {
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if (result.isEmpty()) {
            log.info("Cart of the member is not exist!!");

            Member member = Member.builder()
                    .email(email)
                    .build();

            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();

            return cartRepository.save(tempCart);
        } else {
            return result.get();
        }
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {
        // 삭제할 CartItem이 속한 Cart 번호 조회
        Long cno = cartItemRepository.getCartFromItem(cino);
        log.info("cart no: " + cno);

        // CartItem 삭제
        cartItemRepository.deleteById(cino);

        // 삭제 후 해당 Cart에 속한 장바구니 아이템 리스트 재조회 및 반환
        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }

}
