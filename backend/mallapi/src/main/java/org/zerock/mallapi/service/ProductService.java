package org.zerock.mallapi.service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

// Pageable	요청 정보	page 번호, 사이즈, 정렬 기준	Controller → Repository 요청 시
// PageRequest	Pageable 구현체	위와 같음 (page, size, sort)	Pageable을 만들 때 사용
// Page<T>	응답 데이터	결과 리스트 + 페이지 메타 정보	Repository → Controller 응답 시

@Transactional
public interface ProductService {
   public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);
   //Page<T>는 List<T>(=ArrayList)처럼 데이터를 담으면서, 거기에 “페이지 관련 정보”와 메소드까지 추가로 제공하는 고급 객체입니다.
   Long register(ProductDTO productDTO);

   ProductDTO get(Long pno);

   void modify(ProductDTO productDTO);

   void remove(Long pno);
}
