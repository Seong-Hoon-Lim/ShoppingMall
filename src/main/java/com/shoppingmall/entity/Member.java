package com.shoppingmall.entity;

import com.shoppingmall.constant.Role;
import com.shoppingmall.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

/**
 * DB에 회원정보를 저장하는 클래스
 * 관리할 회원정보는 이름, 이메일, 비밀번호, 주소, 역할
 */
@Entity
@Table(name = "member")
@Setter
@Getter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    //회원가입 시 중복된 회원이 있는지 이메일로 검증
    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String addr;

    @Enumerated(EnumType.STRING)
    private Role role;

    /*
     * Member entity를 생성하는 메소드
     * 별도로 엔티티를 생성해서 관리를 하면
     * 코드가 변경되더라도 한 군데만 수정하면 되는 이점이 있음.
     */
//    public static Member createMember(MemberDTO memberDTO,
//                                      PasswordEncoder encoder) {
//        Member member = new Member();
//        member.setName(memberDTO.getName());
//        member.setEmail(memberDTO.getEmail());
//        member.setAddr(memberDTO.getAddr());
//        /*
//         스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을
//         파라미터로 넘겨서 비밀번호를 암호화 처리
//         */
//        String password = encoder.encode(memberDTO.getPassword());
//        member.setPassword(password);
//        member.setRole(Role.USER);
//        return member;
//    }

    /*
     * Member entity를 생성하는 메소드
     * 별도로 엔티티를 생성해서 관리를 하면
     * 코드가 변경되더라도 한 군데만 수정하면 되는 이점이 있음.
     * ADMIN 계정으로 생성
     */
    public static Member createMember(MemberDTO memberDTO,
                                      PasswordEncoder encoder) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAddr(memberDTO.getAddr());
        /*
         스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을
         파라미터로 넘겨서 비밀번호를 암호화 처리
         */
        String password = encoder.encode(memberDTO.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }


}
