package com.shoppingmall.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 장바구니에는 고객의 관심이 있거나 나중에 사려는 상품들을 담아두므로
 * 하나의 장바구니에는 여러개의 상품들이 들어갈 수 있다.
 * 또한 같은 상품을 여러개 주문할 수도 있으므로 몇개를 담아줄 것인지
 * 설정해둬야함
 * 이런 장바구니에 담아둘 상품을 정의하는 엔티티 클래스로
 * Cart 와 Item 엔티티 클래스와 다대일 단방향 매핑을 설정
 */
@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;  //하나의 장바구니에는 여러개의 상품을 담을 수 있으므로 다대일 관계 매핑

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  //하나의 상품은 여러 장바구니의 장바구니 상품으로 담기므로 다대일 관계 매핑

    private int count;  //같은 상품을 장바구니에 몇개 담을지 저장

}
