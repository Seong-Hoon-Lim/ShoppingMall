package com.shoppingmall.repository;

import com.shoppingmall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /* 회원가입 시 중복된 회원이 있는지 이메일로 검증하기 위한 쿼리 메소드 */
    Member findMemberByEmail(String email);

}
