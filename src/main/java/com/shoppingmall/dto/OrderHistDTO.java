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
        this.orderDate =
                order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemDTO(OrderItemDTO orderItemDTO) {
        orderItemDTOList.add(orderItemDTO);
    }

}
