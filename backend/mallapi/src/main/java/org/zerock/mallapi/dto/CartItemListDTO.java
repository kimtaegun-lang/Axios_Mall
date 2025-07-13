package org.zerock.mallapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;
    private int qty;
    private Long pno;
    private String pname;
    private int price;
    private String imageFile;

    // JPQL을 이용해서 직접 DTO 객체를 생성하는 Projection이라는 방식을 이용하기 위함이다.
    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
