package com.sungil.springboot.databrainbackend.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * [JWT Authentication Filter]
 * HTTP 요청 헤더에서 JWT 토큰을 추출하고, 유효성을 검증하여 SecurityContext에 인증 정보를 등록합니다.
 * * 시니어급 포인트:
 * 1. OncePerRequestFilter 상속: 서블릿 필터의 중복 실행을 방지하여 자원 낭비를 막습니다.
 * 2. SecurityContextHolder 관리: 인증 성공 시에만 Context를 설정하여 무분별한 메모리 점유를 지양합니다.
 * * @author Senior Backend Engineer
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터의 핵심 로직입니다. 모든 요청은 이 메서드를 거쳐갑니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 JWT 토큰을 추출합니다.
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰이 존재하고, 유효성 검사를 통과한다면 인증 처리를 진행합니다.
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            try {
                // 토큰으로부터 인증 객체(Authentication)를 생성합니다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // Spring Security의 전역 저장소인 SecurityContextHolder에 인증 정보를 저장합니다.
                // 이후 로직(Controller 등)에서 이 유저가 누구인지 알 수 있게 됩니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Authenticated user '{}', setting security context", authentication.getName());
            } catch (Exception e) {
                // 인증 과정에서 예외가 발생하면 Context를 비워 보안을 유지합니다.
                SecurityContextHolder.clearContext();
                log.error("Could not set user authentication in security context", e);
            }
        }

        // 3. 다음 필터로 요청을 넘깁니다. (필수!)
        filterChain.doFilter(request, response);
    }
}