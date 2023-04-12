package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DB와 관계없이 회원가입 view로 부터 넘어오는
 * 가입정보를 담을 DTO 클래스
 */

@Getter
@Setter
public class MemberDTO {

    private String name;
    private String email;
    private String passwd;
    private String addr;
}
