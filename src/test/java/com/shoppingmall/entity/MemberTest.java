package com.shoppingmall.entity;

import com.shoppingmall.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository repository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    //@WithMockUser: 스프링 시큐리티에서 제공하는 어노테이션으로 지정한 사용자가 로그인한 상태로 가정하고 테스트
    @WithMockUser(username = "tester", roles = "USER")
    public void auditingTest() {

        Member newMember = new Member();
        repository.save(newMember);

        em.flush();
        em.clear();

        Member member = repository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("create time : " + member.getRegisteredTime());
        System.out.println("update time : " + member.getUpdatedTime());
        System.out.println("create member : " + member.getRegisteredWorker());
        System.out.println("update member : " + member.getUpdatedWorker());
    }

}