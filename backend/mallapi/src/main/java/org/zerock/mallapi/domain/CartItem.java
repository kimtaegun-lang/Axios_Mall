package org.zerock.mallapi.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = "cart")
@Table(
    name = "tbl_cart_item",
    indexes = {
        @Index(columnList = "cart_cno", name = "idx_cartitem_cart"),
        @Index(columnList = "product_pno, cart_cno", name = "idx_cartitem_pno_cart")
    }
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino;

    @ManyToOne // 다쪽에서 manytoone을선언하므로 이쪽이다, 그리고 joincolumn을 선언하므로 주
    @JoinColumn(name = "product_pno")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;

    private int qty;

    public void changeQty(int qty) {
        this.qty = qty;
    }
}
