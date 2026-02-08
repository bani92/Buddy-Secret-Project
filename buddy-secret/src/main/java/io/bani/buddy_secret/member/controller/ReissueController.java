package io.bani.buddy_secret.member.controller;


import io.bani.buddy_secret.global.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. 쿠키에서 Refresh 토큰 꺼내기
        String refresh = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // 2. 토큰 만료 여부 확인 및 category가 "refresh"인지 확인
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        if(jwtUtil.isExpired(refresh)) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 3. Redis에 저장되어 있는지 확인 (로그아웃 여부 및 일치 확인)
        String username = jwtUtil.getUsername(refresh);
        String saveRefresh = redisTemplate.opsForValue().get("refresh:" + username);

        if (saveRefresh == null || !saveRefresh.equals(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // ---------------------------------------------------
        // 여기까지 통과했다면 새로운 토큰 발급 시작!
        // ---------------------------------------------------

        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 60000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        redisTemplate.delete("refresh:" + username);
        redisTemplate.opsForValue().set("refresh:" + username, newRefresh, 24, TimeUnit.HOURS);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh",newRefresh));

        log.info("토큰 재발급 성공: {}", username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // cookie.setSecure(true); // HTTPS 적용 시 활성화
        return cookie;
    }
}
