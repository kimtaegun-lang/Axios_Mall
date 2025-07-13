package org.zerock.mallapi.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Member;
public interface MemberRepository extends JpaRepository<Member,String>{
    @EntityGraph(attributePaths = {"memberRoleList"}) // @EntityGraph는 JPA에서 연관된 엔티티를 한꺼번에 조회할 때 사용하는 기능입니다.
    // 즉시 로딩(Eager Fetching) 하도록 강제하는 효과를 냅니다
    @Query("select m from Member m where m.email=:email")
    Member getWithRoles(@Param("email") String email);
    
}
