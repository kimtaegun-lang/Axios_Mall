package org.zerock.mallapi.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long>{
    
    @EntityGraph(attributePaths="imageList") // . JPA에서는 쿼리를 작성할 때 @EntityGraph를 이용해서 해당 속성을 조인 처리하도록 설정해 줄 수 있다. JPQL방식
    // 해당 엔티티를 미리 생성한다는 느낌?
    @Query("select p from Product p where p.pno=:pno")
    Optional<Product>selectOne(@Param("pno")Long pno);

    @Modifying // @Modifying은 @Query와 함께 "데이터 변경 쿼리" 를 쓸 때 필수로 붙여야 해.
    @Query("update Product p set p.delFlag=:flag where p.pno=:pno")
    void updateToDelete(@Param("pno") Long pno,@Param("flag") boolean flag);

    @Query("select p,pi from Product p left join p.imageList pi where pi.ord=0 and p.delFlag=false")
    Page<Object[]>selectList (Pageable pageable);
    // ord: 대표이미지 delFlag: 삭제여부 pi는 p테이블의 이미지 리스트라고 생각, 이걸 레프트 조인 (즉, 삭제되지 않앗고 대표이미지가 0인 이미지 리스트, 테이블을 가져옴?)
    // pageable 사용자가 요청할 때 → "2페이지 주세요, 사이즈는 10개요"  Page 응답할 때 → "여기 2페이지에 해당하는 데이터 10개, 전체는 45개 있어요"
}
