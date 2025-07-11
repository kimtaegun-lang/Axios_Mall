package org.zerock.mallapi.domain;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable// 어떤 엔티티가 여러 개의 관련 속성을 묶어서 관리하고 싶을 때,

//그 속성들을 별도의 클래스로 만들고,

///그 클래스를 @Embeddable로 표시하면

//엔티티에 해당 클래스를 필드로 포함시켜 사용할 수 있어요
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String fileName;
    private int ord;
    public void setOrd(int ord) {
        this.ord=ord;
    }
}
