package com.shoppingmall.repository;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.entity.Cart;
import com.shoppingmall.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder encoder;

    @PersistenceContext
    EntityManager em;

    //회원 엔티티 생성
    public Member createMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail("spring@gmail.com");
        memberDTO.setName("가길동");
        memberDTO.setAddr("경기도 평택시 청북읍 판교길 9");
        memberDTO.setPassword("11111111");
        return Member.createMember(memberDTO, encoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        /*
         em.flush(): 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 메소드를 호출하여 DB에 반영
         회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로 flush() 를 호출하여
         DB 반영

         em.clear(): 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우 DB를 조회
         실제 DB에서 장바구니 엔티티를 가지고 올때 회원 엔티티도 같이 가지고 오는지 보기 위해 영속성 컨텍스트를 비움.
         */
        em.flush();
        em.clear();

        //저장된 장바구니 엔티티를 조회
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        //처음에 저장한 member 엔티티의 id와 savedCart에 매핑된 member 엔티티의 id를 비교
        assertEquals(savedCart.getMember().getId(), member.getId());

    }



}