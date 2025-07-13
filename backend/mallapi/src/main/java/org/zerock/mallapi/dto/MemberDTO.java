package org.zerock.mallapi.dto;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User // spring security 때문에 user을 상속받는다 (사용자생성이 아닌 제공받는것)
{
    private String email;
    private String pw;
    private String nickname;
    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email, pw,
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));
                // Spring Security가 이해할 수 있는 권한 객체로 바꿔주는 작업 (부모 클래스인 user의 생성자를 생성시킨다.)
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() { // JWT 토큰 발급 시 사용할 사용자 정보를 Map 형태로 뽑아주는 메서드 이 Map을 JWT의 **Claims(주장, payload)**로 넣을 수 있음

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);
        return dataMap;
    }
}
