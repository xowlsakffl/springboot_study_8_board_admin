package com.fastcampus.boardadmin.config;

import com.fastcampus.boardadmin.dto.security.BoardAdminPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())// 인증 정보를 불러온다.
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)// 인증 되었는지
                .map(Authentication::getPrincipal)
                .map(BoardAdminPrincipal.class::cast)
                .map(BoardAdminPrincipal::getUsername);
    }
}
