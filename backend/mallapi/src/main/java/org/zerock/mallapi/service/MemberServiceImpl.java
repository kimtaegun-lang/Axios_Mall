package org.zerock.mallapi.service;

import org.springframework.http.HttpHeaders;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;
import org.zerock.mallapi.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) { // 카카오 API에 access token을 사용해 HTTP GET 요청을 보내서, 카카오 서버로부터 해당
                                                          // 토큰에 연동된 사용자 계정 정보(특히 이메일) 를 받아오는 역할
        String email = getEmailFromKakaoAccessToken(accessToken);
        log.info("email: " + email);
        Optional<Member> result = memberRepository.findById(email);

        // 기존의 회원
        if (result.isPresent()) { // Optional에 값이 존재하는지 확인하는 메서드입니다
            MemberDTO memberDTO = entityToDTO(result.get()); // result.get() Optional 안에 들어 있는 실제 값을 꺼냅니다. member객체
            return memberDTO;
        }

        // 회원이 아니었다면 닉네임은 '소셜회원’으로 패스워드는 임의로 생성
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);
        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    // 카카오 페이지에서 회원 정보 받아오기
    private String getEmailFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate(); // Spring에서 제공하는 HTTP 통신 클라이언트 외부 API(이 경우 카카오 API)에 요청을 보낼 때 사용
                                                 
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded"); // 폼 데이터 방식으로 요청한다는 의미
        HttpEntity<String> entity = new HttpEntity<>(headers); // 요청 본문 없이 헤더만 포함한 HTTP 요청 객체를 생성합니다.
        UriComponents uriBuilder = UriComponentsBuilder.fromUriString(kakaoGetUserURL).build(); // 요청할 URL을 생성합니다
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, 
                LinkedHashMap.class); // 응답은 LinkedHashMap으로 받아옴 (JSON 구조가 Map처럼 오기 때문에)

        log.info(response);
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody(); // 카카오에서 받은 사용자 정보는 JSON 형식인데 그걸 Map으로 받아서 가공
        log.info("	");

        log.info(bodyMap);
        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount: " + kakaoAccount);
        return kakaoAccount.get("email"); // 이메일 부분 꺼냄
    }

    private String makeTempPassword() // 10자리 난수 만들기
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }
        return buffer.toString();
        // 문자열을 여러번 이어붙일 때 StringBuffer를 쓰는 이유는 문자열을 직접 더하면 매번 새로운 객체가 만들어져 비효율적이기 때문
    }

    // 회원 생성 메소드
    private Member makeSocialMember(String email) {
        String tempPassword = makeTempPassword(); // 10개 난수
        log.info("tempPassword: " + tempPassword);
        String nickname = "소셜회원";
        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword)) // 소셜 로그인시, 이메일만 필요하므로 비밀번호 난수를 생성
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }

    
    // 멤버 수정
    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {

        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();

        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);
        member.changeNickname(memberModifyDTO.getNickname());

        memberRepository.save(member);

    }
}
