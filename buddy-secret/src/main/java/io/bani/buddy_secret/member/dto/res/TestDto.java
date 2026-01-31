package io.bani.buddy_secret.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 명시 (가끔 필요할 때가 있음)
@AllArgsConstructor
public class TestDto {
    private String name;
    private String email;
}
