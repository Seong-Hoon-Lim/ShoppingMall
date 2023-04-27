package com.shoppingmall.entity;

import com.shoppingmall.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문정보를 저장하는 Order 엔티티 클래스
 * 회원 엔티티와 다대일 단방향 매핑
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //한명의 회원은 여러번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑
    
    private LocalDateTime orderDate;    //주문일
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태
    
    /*
     주문 상품 엔티티와 일대다 매핑. 외래키(order_id)가 order_item 테이블에 있으므로
     연관관계의 주인은 OrderItem 엔티티임. Order 엔티티가 주인이 아니므로 mappedBy 속성으로
     연관관계의 주인을 설정. @OneToMany(mappedBy = "order") 의미는 OrderItem 에 있는
     Order 에 의해 관리됨을 의미
     연관관계의 주인의 필드를 mappedBy 값으로 세팅하면 됨
     
     부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll 옵션을 설정
     고객이 주문할 상품을 선택하고 주문할 때 주문 엔티티를 저장하면서 주문 상품 엔티티도 함께 저장되는 경우
     
     orphanRemoval = true: 고아 객체 제거 옵션
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); //여러개 주문 상품을 갖게 되므로 리스트 저장

//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;

    /*
      orderItems 에 주문 상품 정보들을 추가하는 메소드
      orderItem 객체를 order 객체의 orderItems 에 추가
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /*
     Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계 이므로,
     orderItem 객체에도 order 객체를 세팅해줌.
     */
    public static Order createOrder(Member member,
                                    List<OrderItem> orderItems) {
        Order order = new Order();
        //상품을 주문한 회원의 정보를 세팅
        order.setMember(member);
        /*
         상품 페이지에서는 1개의 상품을 주문하지만, 장바구니 페이지에서는 한 번에
         여러개의 상품을 주문할 수 있음. 따라서 여러개의 주문 상품을 담을 수 있도록
         리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가함.
         */
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        //주문이 생성되면 주문상태는 ORDER 로 세팅됨
        order.setOrderStatus(OrderStatus.ORDER);
        //현재 시간을 주문 시간으로 세팅함.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /*
     총 주문 금액을 구하는 메소드
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    
}
