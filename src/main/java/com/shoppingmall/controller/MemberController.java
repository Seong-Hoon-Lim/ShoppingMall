package com.shoppingmall.controller;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.entity.Member;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/members/*")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;
    private final PasswordEncoder encoder;

    /* 회원가입 페이지 */
    @GetMapping(value = "register")
    public String registerMemberPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/registerMember";
    }

    /*
      회원가입 처리
      검증하려는 객체 앞에 @Valid 어노테이션 선언해서 사용
      파라미터는 bindingResult 객체를 추가하여 검사 결과를 담는다
      bindingResult.hasErrors() 메소드를 호출하면 에러가 있을 시
      회원가입 페이지로 리다이렉트
      
      회원가입 시 중복 회원 가입 예외가 발생하면 에러 메시지를 뷰로 전달
     */
    @PostMapping(value = "register")
    public String registerMemberProcess(@Valid MemberDTO memberDTO,
                                        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/registerMember";
        }
        try {
            Member member = Member.createMember(memberDTO, encoder);
            service.saveMember(member);
        }
        catch (IllegalStateException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "member/registerMember";
        }
        return "redirect:/";

    }

    /* 로그인 페이지 */
    @GetMapping(value = "login")
    public String loginPage() {
        return "member/loginMember";
    }

    /* 로그인 에러 처리 (GET 방식)*/
    @GetMapping(value = "login/error")
    public String loginErrorMsg(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/loginMember";
    }

//    @PostMapping(value = "login")
//    public String loginProcess() {
//
//        return "redirect:/";
//    }

}
