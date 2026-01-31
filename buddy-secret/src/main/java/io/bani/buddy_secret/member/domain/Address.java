package io.bani.buddy_secret.member.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주소 정보")
public class Address {

    @Schema(description = "우편번호")
    private String zipcode;

    @Schema(description = "기본주소")
    private String addressBasic;

    @Schema(description = "상세주소")
    private String addressDetail;
}
