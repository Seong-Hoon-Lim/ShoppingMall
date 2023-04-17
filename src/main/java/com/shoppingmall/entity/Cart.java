package com.shoppingmall.entity;

import lombok.*;
import javax.persistence.*;

/**
 * 장바구니 엔티티 클래스
 * 회원 엔티티와 1 대 1 의 매핑관계와 동시에
 * 장바구니 엔티티가 회원 엔티티를 참조하는 단방향 매핑
 * 이렇게 매핑된 경우 장바구니 엔티티를 조회하면서 동시에
 * 회원 엔티티의 정보도 동시에 가져올 수 있음
 */
@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
     @OneToOne: 일대일 매핑관계를 지을 때 사용
      - 엔티티를 조회할 때 해당 엔티티와 매핑된 엔티티도 한 번에 조회하는 것을 즉시 로딩이라 함
        OneToOne, ManyToOne 으로 매핑할 경우 즉시 로딩을 기본 Fetch 전략으로 설정
        fetch = FetchType.EAGER 과 같음

     @JoinColumn: 매핑할 외래키를 지정
                  name 속성에는 매핑할 외래키의 이름을 설정
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

}
