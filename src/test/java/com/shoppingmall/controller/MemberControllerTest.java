package com.shoppingmall.controller;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.entity.Member;
import com.shoppingmall.service.MemberService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService service;

    /*
     MockMvc 클래스를 활용하여 실제 객체와 비슷하지만 테스트에
     필요한 기능만 가지는 가짜 객체 생성
     웹 브라우저에 요청을 하는 것 처럼 테스트 가능
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder encoder;

    /*
     테스트를 위해 회원 정보 생성
     */
    public Member createMember(String email, String passwd) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setName("나길동");
        memberDTO.setAddr("경기도 평택시 청북읍 판교길 9");
        memberDTO.setPassword(passwd);
        Member member = Member.createMember(memberDTO, encoder);
        return service.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "spring@gmail.com";
        String passwd = "11111111";
        this.createMember(email, passwd);

        /*
         회원 가입 메소드를 실행 후 가입된 회원 정보로 로그인이 되는지 테스트
         userParameter() 를 이용하여 이메일을 아이디로 세팅하고 로그인 URL에 요청       
        */
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(passwd))
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //로그인 성공 인증 시 테스트 코드 통과
    }
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "spring@gmail.com";
        String passwd = "11111111";
        this.createMember(email, passwd);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("11111112"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}