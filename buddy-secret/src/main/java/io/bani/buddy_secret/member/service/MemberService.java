package io.bani.buddy_secret.member.service;

import io.bani.buddy_secret.global.util.AES256Util;
import io.bani.buddy_secret.member.domain.Address;
import io.bani.buddy_secret.member.domain.Member;
import io.bani.buddy_secret.member.dto.req.MemberJoinReqDto;
import io.bani.buddy_secret.member.dto.res.MemberResDto;
import io.bani.buddy_secret.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return memberRepository.save(member).getId();
    }

    @Transactional
    public void withdrawMember(String email) {
        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        memberRepository.delete(member);
    }
}
