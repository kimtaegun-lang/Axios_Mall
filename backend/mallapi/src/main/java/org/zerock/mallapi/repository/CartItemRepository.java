package org.zerock.mallapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemListDTO;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 1. 이메일로 해당 회원의 장바구니 아이템 목록을 DTO로 조회
    @Query(
        "select " +
        "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, " + // JPQL에서 DTO(일명 Projection) 생성자 호출 문법입니다. 즉, 조회 결과를 CartItemListDTO 객체의 생성자 파라미터로 바로 매핑해서 반환합니다.
        "CASE WHEN pi IS NULL THEN null ELSE pi.fileName END) " + // 대표이미지가 없으면 null , 있으면 파일이름
        "from CartItem ci " + 
        "inner join Cart mc on ci.cart = mc " +
        "left join Product p on ci.product = p " +
        "left join p.imageList pi on pi.ord = 0 " +
        "where mc.owner.email = :email " +
        "order by ci desc"
    )
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 2. 특정 회원(email)의 장바구니에서 특정 상품(pno)에 해당하는 CartItem 조회
    @Query(
        "select ci " +
        "from CartItem ci inner join Cart c on ci.cart = c " +
        "where c.owner.email = :email and ci.product.pno = :pno"
    )
    CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    // 3. CartItem의 cino(아이템번호)로 해당 아이템이 속한 Cart의 cno(장바구니 번호) 조회
    @Query(
        "select c.cno " +
        "from Cart c inner join CartItem ci on ci.cart = c " +
        "where ci.cino = :cino"
    )
    Long getCartFromItem(@Param("cino") Long cino);

    // 4. Cart 번호(cno)로 장바구니 아이템 목록을 DTO로 조회 (이미지 파일은 ord=0인 것만)
    @Query(
        "select " +
        "new org.zerock.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) " +
        "from CartItem ci inner join Cart mc on ci.cart = mc " +
        "left join Product p on ci.product = p " +
        "left join p.imageList pi " +
        "where mc.cno = :cno and pi.ord = 0 " +
        "order by ci desc"
    )
    List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);
}
