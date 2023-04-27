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

    /*
     주문할 상품과 주문 수량을 통해 OrderItem 객체를 만드는 메소드
     */
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        //주문 상품
        orderItem.setItem(item);
        //주문 수량
        orderItem.setCount(count);
        //현재 시간을 기준으로 상품 가격을 주문 가격으로 세팅
        orderItem.setOrderPrice(item.getPrice());

        //주문 수량만큼 상품의 재고 수량을 감소 시킴
        item.removeStock(count);
        return orderItem;
    }

    //주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드
    public int getTotalPrice() {
        return orderPrice * count;
    }

}
