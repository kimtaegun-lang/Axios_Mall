package org.zerock.mallapi.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration() // ModelMapper의 매핑 설정(configuration)을 가져옵니다.
        .setFieldMatchingEnabled(true) // 필드 이름 기반의 직접 매핑을 활성화 기본적으로는 getter/setter 메서드 기준인데, 이 옵션을 켜면 private 필드도 직접 접근해서 매핑 가능해짐
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // 필드 직접 접근 권한 수준 지정 PRIVATE로 설정해서 private 필드도 리플렉션으로 접근 가능하게 함
        .setMatchingStrategy(MatchingStrategies.LOOSE); //  필드명이 완전히 같지 않아도(약간 다른 이름, 케이스 등) 매핑을 시도함 strict도 존재한다.
        return modelMapper;
    }
}
