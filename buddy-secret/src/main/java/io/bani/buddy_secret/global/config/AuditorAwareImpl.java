package io.bani.buddy_secret.global.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        // 1. 나중에 스프링 시큐리티를 붙이면 여기서 세션 정보를 꺼냅니다.
        // 2. 세션에 저장된 사용자의 PK(Long) 값을 리턴하게 됩니다.

        // 현재는 테스트 단계이므로, 시스템 관리자 ID(예: 0L)를 보내거나
        // 일단 empty를 반환하도록 설정해둡니다.
        return Optional.of(0L);
    }
}
