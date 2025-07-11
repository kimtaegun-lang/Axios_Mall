package org.zerock.mallapi.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data // 게터 세터 메소드 생략
@Builder // 객체 생성 쉽게 해줌
@AllArgsConstructor // 파라미터를 받는 생성자 자동 생성
@NoArgsConstructor // 파라미터가 없는 생성자 자동 생성
public class TodoDto {
    private Long tno;
    private String title;
    private String writer;
    private boolean complete;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd") // 데이터 타입을 문자열로 변경하고 형식은 yyyy-mm-dd 그리고 json형태로 넘김.
    private LocalDate dueDate;
}
