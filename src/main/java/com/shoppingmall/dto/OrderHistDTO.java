package com.shoppingmall.dto;

import com.shoppingmall.constant.OrderStatus;
import com.shoppingmall.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 정보를 담는 클래스
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistDTO {

    private Long orderId;  //주문번호

    private String orderDate;   //주문날짜

    private OrderStatus orderStatus;  //주문상태

    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public OrderHistDTO(Order order) {
        this.orderId = order.getId();
        //주문날짜의 경우 yyyy-MM-dd HH:mm 형태로 포맷 수정
        this.orderDate =
                order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    /*
     orderItemDTO 객체를 주문 상품 리스트에 추가하는 메소드
     */
    public void addOrderItemDTO(OrderItemDTO orderItemDTO) {
        orderItemDTOList.add(orderItemDTO);
    }

}
