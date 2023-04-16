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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder encoder;

    public Member createMember(String email, String passwd) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setName("나길동");
        memberDTO.setAddr("경기도 평택시 청북읍 판교길 9");
        memberDTO.setPasswd(passwd);
        Member member = Member.createMember(memberDTO, encoder);
        return service.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "spring@gmail.com";
        String passwd = "11111111";
        this.createMember(email, passwd);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(passwd))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());

    }


}