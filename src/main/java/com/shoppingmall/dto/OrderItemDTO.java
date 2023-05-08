package com.shoppingmall.dto;

import com.shoppingmall.entity.OrderItem;
import lombok.*;

/**
 * 조회한 주문 데이터를 화면에 보낼 때 사용할
 * DTO 클래스 주문 상품 정보를 담고 있음
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private String itemName;  //상품명

    private int count;  //주문 수량

    private int orderPrice; //주문금액

    private String imgUrl;  //상품 이미지 경로

    /*
     orderItem 객체와 이미지 경로를 파라미터로 받아
     멤버 변수의 값을 세팅
     */
    public OrderItemDTO(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
