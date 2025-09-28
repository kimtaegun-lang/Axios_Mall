package org.zerock.mallapi.service;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;

@Transactional
public interface MemberService {
 MemberDTO getKakaoMember(String accessToken);

 void modifyMember(MemberModifyDTO MemberModifyDTO);
 
 default MemberDTO entityToDTO(Member member) // default 키워드는 Java 인터페이스 내에 구현이 포함된 메서드를 정의
 {
	 MemberDTO dto = new MemberDTO( member.getEmail(), member.getPw(), 
    member.getNickname(), member.isSocial(),
    member.getMemberRoleList().stream() // stream()은 이 리스트의 각 원소(여기서는 memberRole)를 하나씩 순차적으로 처리할 수 있게 해줌
    .map(memberRole -> memberRole.name()).collect(Collectors.toList()));

return dto;
 }
}
