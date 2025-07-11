package org.zerock.mallapi.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder // builder 에 상속 기능 넣어 둿다 생각하기
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
     @Builder.Default
    private int page=1;
    @Builder.Default
    private int size=10;
}
