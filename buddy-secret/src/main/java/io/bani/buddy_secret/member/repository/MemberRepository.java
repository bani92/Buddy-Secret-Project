package io.bani.buddy_secret.member.repository;

import io.bani.buddy_secret.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 로그인 시 이메일로 회원 정보를 조회
     */
    Optional<Member> findByEmail(String email);

    /**
     * 회원가입시 이메일 중복 체크
     */
    boolean existsByEmail(String email);


}
