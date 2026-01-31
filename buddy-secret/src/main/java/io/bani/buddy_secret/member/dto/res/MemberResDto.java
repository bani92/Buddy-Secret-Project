package io.bani.buddy_secret.member.dto.res;

import io.bani.buddy_secret.member.domain.Address;
import io.bani.buddy_secret.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 정보 response DTO")
public class MemberResDto {

    @Schema(description = "회원 고유 번호(PK)")
    private Long id;

    @Schema(description = "이메일")
    private String email;

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

    @Schema(description = "생성자_ID")
    private Long createdId;

    @Schema(description = "생성일시")
    private LocalDateTime createdDttm;

    @Schema(description = "수정자_ID")
    private Long updatedId;

    @Schema(description = "수정일시")
    private LocalDateTime updatedDttm;

    // 엔티티를 DTO로 변환하는 생성자
    public MemberResDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.birthDate = member.getBirthDate();
        this.phoneNumber = member.getPhoneNumber();
        this.address = member.getAddress();
        this.role = member.getRole();
        this.status = member.getStatus();
        this.provider = member.getProvider();
        this.kakaoId = member.getKakaoId();
        this.naverId = member.getNaverId();
        this.googleId = member.getGoogleId();
        this.createdId = member.getCreatedId();
        this.createdDttm = member.getCreatedDttm();
        this.updatedId = member.getUpdatedId();
        this.updatedDttm = member.getUpdatedDttm();

    }
}
