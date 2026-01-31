package io.bani.buddy_secret.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "회원가입 request DTO")
public class MemberJoinReqDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "buddy@secret.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Schema(description = "비밀번호")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "이름")
    private String name;

    @NotBlank(message = "생년월일은 필수입니다.")
    @Schema(description = "생년월일")
    private String birthDate;

    @NotBlank(message = "핸드폰번호는 필수입니다.")
    @Schema(description = "핸드폰번호")
    private String phoneNumber;

    @NotBlank(message = "우편번호는 필수입니다.")
    @Size(min = 5, max = 5, message = "우편번호는 5자리여야 합니다.")
    @Schema(description = "우편번호")
    private String zipcode;

    @NotBlank(message = "기본주소는 필수입니다.")
    @Schema(description = "기본주소")
    private String addressBasic;

    @Schema(description = "상세주소")
    private String addressDetail;

}
