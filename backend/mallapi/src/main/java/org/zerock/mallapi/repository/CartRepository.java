package org.zerock.mallapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.zerock.mallapi.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where cart.owner.email = :email")
    // 메서드를 호출하는 쪽에서 넘긴 email 값이 쿼리의 :email 자리에 들어가는 것 @Param("email")
    Optional<Cart> getCartOfMember(@Param("email") String email);
}
