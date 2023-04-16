package com.shoppingmall.service;

import com.shoppingmall.entity.Member;
import com.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Transactional : 비즈니스 로직을 실행 중 에러 발생 시 변경된 데이터 로직을
 * 수행하기 이전 상태로 롤백 시켜주는 기능
 * @RequiredArgsConstructor : 빈을 주입하는 방법으로 final 이나 @NonNull 이 붙은
 * 필드에 생성자를 생성해줌
 * 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면
 * @Autowired 불필요
 * 
 * UserDetailsService 인터페이스는 DB에서 회원정보를 가져오는 역할
 * loadUserByUsername() 메소드가 동작되어 사용자의 정보와 권한을 갖는
 * UserDetails 인터페이스를 반환
 * 스프링 시큐리티에서 UserDetailsService 구현하고 있는 클래스를 통해 로그인 기능을 구현함
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository repository;

    /* 회원 등록 기능 */
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return repository.save(member);
    }

    /*
     이미 가입된 회원이 있는지 검증하는 메소드
     내부에서 실행될 메소드로 private 접근제어자 설정
     */
    private void validateDuplicateMember(Member member) {
        Member findMember = repository.findMemberByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /*
     UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩 하여
     로그인할 유저의 email을 파라미터로 전달 받아 UserDetail 을 구현하고 있는 User 객체를 반환
     User 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role 을 파라미터로 넘겨줌
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = repository.findMemberByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
