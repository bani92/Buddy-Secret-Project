package io.bani.buddy_secret.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "로그인 request DTO")
public class MemberLoginReqDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "buddy@secret.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Schema(description = "비밀번호")
    private String password;
}
