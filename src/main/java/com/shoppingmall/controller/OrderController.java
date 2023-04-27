package com.shoppingmall.controller;

import com.shoppingmall.dto.OrderDTO;
import com.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * 주문 관련 요청들을 처리하기 위한 컨트롤러 클래스
 * 상품 주문에서 웹 페이지의 새로 고침 없이 서버에
 * 주문을 요청하기 위해 비동기 방식을 사용
 */
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 스프링에서 비동기 처리를 할 때
     * @ResponseBody @RequestBody 어노테이션을 사용
     * @RequestBody: HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달
     * @ResponseBody: 자바 객체를 HTTP 요청의 body로 전달
     *
     * @param orderDTO
     * @param bindingResult
     * @param principal
     * @return
     */
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order
            (@RequestBody @Valid OrderDTO orderDTO,
             BindingResult bindingResult, Principal principal) {

        //주문 정보를 받는 orderDTO 객체에 데이터 바인딩 시 에러 여부 검사
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors =
                    bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            //에러 정보를 ResponseEntity 객체에 담아서 반환
            return new ResponseEntity<String>(sb.toString(),
                    HttpStatus.BAD_REQUEST);
        }

        /*
         현재 로그인 유저의 정보를 얻기 위해 @Controller 어노테이션이 선언된
         클래스에서 메소드 인자로 principal 객체를 넘겨줄 경우 해당 객체에
         직접 접근할 수 있음.
         principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회함.
         */
        String email = principal.getName();
        Long orderId;

        try {
            //주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출.
            orderId = orderService.order(orderDTO, email);
        }
        catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        //결과 값으로 생성 된 주문번호와 요청이 성공했다는 HTTP 응답 상태 코드 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }

}
