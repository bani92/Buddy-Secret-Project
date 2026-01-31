package io.bani.buddy_secret.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter // 필드 조회용
@NoArgsConstructor // 기본 생성자 필수
@EntityListeners(AuditingEntityListener.class) // 날짜 자동 기록용
@Schema(description = "회원 테이블")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원번호")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "이메일")
    private String email;

    @Column(nullable = false)
    @Schema(description = "패스워드")
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "생년월일")
    private String birthDate;

    @Schema(description = "핸드폰번호")
    private String phoneNumber;

    @Embedded
    private Address address;

    @Schema(description = "권한 (ROLE_USER, ROLE_ADMIN)")
    private String role;

    @Schema(description = "가입 경로 (LOCAL, KAKAO, NAVER, GOOGLE)")
    private String status;

    @Schema(description = "가입경로")
    private String provider;

    @Schema(description = "카카오_ID")
    private String kakaoId;

    @Schema(description = "네이버_ID")
    private String naverId;

    @Schema(description = "구글_ID")
    private String googleId;

    @CreatedBy
    @Column(updatable = false)
    @Schema(description = "생성자_ID")
    private Long createdId;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "DATETIME(0)")
    @Schema(description = "생성일시")
    private LocalDateTime createdDttm;

    @LastModifiedBy
    @Schema(description = "수정자_ID")
    private Long updatedId;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME(0)")
    @Schema(description = "수정일시")
    private LocalDateTime updatedDttm;

    public void updateAddress(Address address) {

        if (address == null) return;
        this.address = address;
    }

    public void updatePassword(String encodedPassword) {
        if(encodedPassword == null || encodedPassword.isBlank() || encodedPassword.length() < 10) {
            throw new IllegalArgumentException("암호화된 비밀번호 형식이 올바르지 않습니다.");
        }
        this.password = encodedPassword;
    }

    public void updatePhoneNumber(String encryptPhone) {
        if(encryptPhone == null || encryptPhone.isBlank() || encryptPhone.length() < 1) {
            throw new IllegalArgumentException("암호화된 핸드폰번호 형식이 올바르지 않습니다.");
        }
        this.phoneNumber = encryptPhone;
    }
}
