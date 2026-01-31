package io.bani.buddy_secret.member.controller;

import io.bani.buddy_secret.member.dto.req.MemberJoinReqDto;
import io.bani.buddy_secret.member.dto.res.MemberResDto;
import io.bani.buddy_secret.member.dto.res.TestDto;
import io.bani.buddy_secret.member.service.MemberService;
import io.bani.buddysecretcore.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Member", description = "회원 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    // 1. ModelMapper & ApiResponse 테스트
    @GetMapping("/test/success")
    public ApiResponse<TestDto> testSuccess(@RequestParam String name) {
        Map<String, String> source = Map.of("name", name, "email", name + "@test.com");

        // Map을 DTO로 변환 (ModelMapper 작동 확인)
        TestDto result = modelMapper.map(source, TestDto.class);

        return ApiResponse.success(result);
    }

    // 2. GlobalExceptionHandler 테스트
    @GetMapping("/test/fail")
    public void testFail() {
        throw new RuntimeException("Core에서 정의한 예외 처리기가 작동합니다!");
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
}
