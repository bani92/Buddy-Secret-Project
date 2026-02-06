package io.bani.buddy_secret.member.service;

import io.bani.buddy_secret.global.util.AES256Util;
import io.bani.buddy_secret.member.domain.Address;
import io.bani.buddy_secret.member.domain.Member;
import io.bani.buddy_secret.member.dto.req.MemberJoinReqDto;
import io.bani.buddy_secret.member.dto.req.MemberLoginReqDto;
import io.bani.buddy_secret.member.dto.res.MemberResDto;
import io.bani.buddy_secret.member.repository.MemberRepository;
import io.bani.buddysecretcore.exception.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AES256Util aes256Util;

    @Transactional(readOnly = true)
    public MemberResDto findByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String decryptPhone = aes256Util.decrypt(member.getPhoneNumber());

        MemberResDto memberResDto = modelMapper.map(member, MemberResDto.class);
        memberResDto.setPhoneNumber(decryptPhone);

        return memberResDto;
    }

    @Transactional
    public Long join(MemberJoinReqDto reqDto) {
        Member member = modelMapper.map(reqDto, Member.class);

        // 1. 중복 체크
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }
        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());

        // 3. 비밀번호 업데이트
        member.updatePassword(encodedPassword);

        // 4. 핸드폰 번호 암호화
        String encryptPhone = aes256Util.encrypt(reqDto.getPhoneNumber());
        member.updatePhoneNumber(encryptPhone);

        // 5. 주소 업데이트
        Address address = new Address(reqDto.getZipcode(), reqDto.getAddressBasic(), reqDto.getAddressDetail());
        member.updateAddress(address);

        // 6. 권한 임시처리
        member.updateRole("ROLE_ADMIN");

        return memberRepository.save(member).getId();
    }

    @Transactional
    public void withdrawMember(String email) {
        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        memberRepository.delete(member);
    }

    public String doComplexLogic(String name) throws InterruptedException {
        log.info("비즈니스 로직 수행 중: {}", name);
        Thread.sleep(500); // 0.5초 대기 (AOP 시간 측정용)
        return "Hello, " + name;
    }

    public void throwError() {
        log.error("의도적인 에러 발생!");
        throw new RuntimeException("테스트용 예외 발생!");
    }
}
