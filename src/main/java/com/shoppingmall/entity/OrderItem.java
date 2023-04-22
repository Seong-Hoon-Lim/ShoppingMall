package com.shoppingmall.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 한번의 주문과 하나의 상품에 대한 여러 주문에 대해
 * 매핑관계 설정을 위한 주문 상품 엔티티 (CartItem 과 역할이 비슷)
 */
@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  //하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 다대일 단방향 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; //한번의 주문에 여러개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 다대일 단방향 매핑

    private int orderPrice; //주문가격

    private int count;  //수량

    //BaseEntity 상속으로 불필요
//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;

}
