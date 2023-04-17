package com.shoppingmall.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    /*
     @WithMockUser: 현재 회원의 이름이 admin이고, role이 ADMIN인
     유저가 로그인된 상태로 테스트를 할 수 있도록 해주는 어노테이션
     */
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemPageTest() throws Exception {

        /*
         상품 등록 페이지에 get 요청을 보냄
         .andDo(print()): 요청과 응답 메시지를 확인할 수 있도록 콘솔창 출력
         .andExpect(status().isOk(): 응답 상태 코드가 정상인지 확인
         */
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/addItem"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    /*
     @WithMockUser: 현재 회원의 이름이 admin이고, role이 ADMIN인
     유저가 로그인된 상태로 테스트를 할 수 있도록 해주는 어노테이션
     */
    @WithMockUser(username = "user", roles = "USER")    //현재 인증된 사용자의 Role을 USER로 세팅
    public void itemPageNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/addForm"))
                .andDo(print())
                //상품 등록 페이지 진입 요청시 Forbidden() 예외가 발생하면 테스트가 성공적으로 통과
                .andExpect(status().isForbidden());
    }


}