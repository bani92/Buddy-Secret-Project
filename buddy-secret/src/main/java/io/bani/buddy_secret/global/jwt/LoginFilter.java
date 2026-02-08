package io.bani.buddy_secret.global.jwt;

import io.bani.buddy_secret.member.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);
        // UsernamePasswordAuthenticationToken DTO역할 , 인증용 바구니
        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달 (검증요청)
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 60000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // Refresh Token Redis에 저장 (Key: 사용자명, Value: 토큰)
        redisTemplate.opsForValue().set("refresh:" + username, refresh, 24, TimeUnit.HOURS);

        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        // cookie.setSecure(true); // HTTPS 프로토콜에서만 쿠키 전송 (운영 시 활성화)
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }


}
