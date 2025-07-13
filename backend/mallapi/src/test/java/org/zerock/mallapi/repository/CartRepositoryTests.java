package org.zerock.mallapi.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Commit;

import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemListDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    // @Test
    public void testInsertByProduct() {
        log.info("test1---------------");

        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 1;

        // 사용자의 장바구니에 동일 상품이 있는지 조회
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem != null) {
            // 있다면 수량 변경 후 저장
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        // 사용자의 장바구니가 존재하지 않으면 새로 생성
        if (result.isEmpty()) {
            log.info("MemberCart is not exist!!");

            Member member = Member.builder()
                    .email(email)
                    .build();

            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();

            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }

        log.info(cart);

        if (cartItem == null) {
            // 상품 엔티티 생성 (pno만 세팅, 실제 DB에 존재하는 상품이어야 함)
            Product product = Product.builder()
                    .pno(pno)
                    .build();

            // 새 장바구니 아이템 생성 (상품, 장바구니, 수량 세팅)
            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .qty(qty)
                    .build();
        }

        // 상품 아이템 저장 (신규 생성 혹은 수량 변경 후 저장)
        cartItemRepository.save(cartItem);
    }

    // @Test
    @Commit
    public void testUpdateByCino() {

        Long cino = 1L; // 장비구니 식별번호
        int qty = 4; // 수량

        Optional<CartItem> result = cartItemRepository.findById(cino);
        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);
        cartItemRepository.save(cartItem); // 수량 변경

    }

    // @Test
    public void testListOfMember() { // 이메일로 장바구니 목록 가져오기

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }

    }

    @Test
    public void testDeleteThenList() {

        Long cino = 1L;

        // cino에 해당하는 CartItem이 속한 Cart 번호 조회
        Long cno = cartItemRepository.getCartFromItem(cino);

        
         cartItemRepository.deleteById(cino);

        // 해당 Cart 번호로 장바구니 아이템 리스트 조회
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }
    }

}
