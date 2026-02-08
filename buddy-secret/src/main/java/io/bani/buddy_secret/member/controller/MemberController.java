package io.bani.buddy_secret.member.controller;

import io.bani.buddy_secret.global.jwt.JWTUtil;
import io.bani.buddy_secret.member.dto.req.MemberJoinReqDto;
import io.bani.buddy_secret.member.dto.res.MemberResDto;
import io.bani.buddy_secret.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Tag(name = "Member", description = "회원 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final JWTUtil jwtUtil;

    @GetMapping("/")
    public String mainP() {
        return "Main Controller";
    }

    @GetMapping("/api/test/success")
    public String success(@RequestParam String name) throws InterruptedException {
        return memberService.doComplexLogic(name);
    }

    @GetMapping("/api/test/error")
    public String error() {
        memberService.throwError();
        return "fail";
    }

    @GetMapping("/api/test/log")
    public String testLogging(@RequestParam String name) {
        // 3. 로그 레벨에 따라 찍어보기
        log.trace("TRACE 레벨: 아주 상세한 정보");
        log.debug("DEBUG 레벨: 개발 단계에서 확인용");
        log.info("INFO 레벨: 일반적인 정보 (기본값)");
        log.warn("WARN 레벨: 경고, 문제는 없지만 주의 필요");
        log.error("ERROR 레벨: 에러 발생! 이름: {}", name); // 중괄호{}를 쓰면 변수 대입이 쉽습니다.

        return "로그가 찍혔습니다. 콘솔을 확인하세요!";
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Long> join(@Valid @RequestBody  MemberJoinReqDto reqDto) {
        return new ResponseEntity<>((memberService.join(reqDto)), HttpStatus.OK);
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/{email}")
    public ResponseEntity<MemberResDto> getMember(@PathVariable String email) {
        return new ResponseEntity<>((memberService.findByEmail(email)), HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> withdraw(@PathVariable String email) {
        memberService.withdrawMember(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authorization.split(" ")[1];

        Long expiration = jwtUtil.getExpiration(token);
        long now = new Date().getTime();
        long remainMs = expiration - now;

        redisTemplate.opsForValue()
                .set(token, "logout", remainMs, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
