package org.zerock.mallapi.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(
    name = "tbl_cart",
    indexes = {
        @Index(name = "idx_cart_email", columnList = "member_owner")
    }
    // 이 부분은 해당 테이블에 인덱스를 생성하라는 뜻입니다.
// 즉, DDL 생성 시 member_owner 컬럼에 대해 인덱스를 생성합니다.
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    @JoinColumn(name = "member_owner") // 이 엔티티(Cart)의 테이블에 **외래 키 컬럼(member_owner)**을 만든다는 의미 owner와 1대1관계
    private Member owner;
}
