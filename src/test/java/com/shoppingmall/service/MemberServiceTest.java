package com.shoppingmall.service;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 회원가입 기능이 정상적으로 동작하는지 여부를 테스트
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService service;

    @Autowired
    PasswordEncoder encoder;

    /*
     회원정보를 입력한 Member 엔티티를 만드는 메소드
     */
    public Member createMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("spring@gmail.com");
        memberDTO.setName("가길동");
        memberDTO.setAddr("경기도 평택시 청북읍 판교길 9");
        memberDTO.setPasswd("11111111");
        return Member.createMember(memberDTO, encoder);
    }

    /*
     Junit의 Assertions 클래스의 assertEquals() 메소드를 이용하면
     저장하려고 요청했던 값과 실제 저장된 데이터를 비교
     첫번째 파라미터에는 기대값, 두번째 파라미터에는 실제 저장된 값을 지정.
     */
    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member savedMember = service.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddr(), savedMember.getAddr());
        assertEquals(member.getPasswd(), savedMember.getPasswd());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    /*
     Junit의 assertThrows() 메소드를 이용 예외 처리 테스트
     첫번째 파라미터에는 발생할 예외 타입을 지정
     assertEquals() 메소드로 발생한 예외 메시지가 예상 결과와 맞는지
     검증.
     */
    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void validateDuplicateMemberTest() {
        Member testMember1 = createMember();
        Member testMember2 = createMember();
        service.saveMember(testMember1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            service.saveMember(testMember2);
        });
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }


}