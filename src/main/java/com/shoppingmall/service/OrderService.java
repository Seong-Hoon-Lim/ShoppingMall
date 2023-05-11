package com.shoppingmall.service;

import com.shoppingmall.dto.OrderDTO;
import com.shoppingmall.dto.OrderHistDTO;
import com.shoppingmall.dto.OrderItemDTO;
import com.shoppingmall.entity.*;
import com.shoppingmall.repository.ItemImgRepository;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.MemberRepository;
import com.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 주문에 대한 비즈니스 로직을 구성하는 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    /* 주문 로직 */
    public Long order(OrderDTO orderDTO, String email) {
        
        //주문할 상품 조회
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        //현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회
        Member member = memberRepository.findMemberByEmail(email);

        List<OrderItem> orderItems = new ArrayList<>();
        //주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        OrderItem orderItem = 
                OrderItem.createOrderItem(item, orderDTO.getCount());
        orderItems.add(orderItem);

        //회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        Order order = Order.createOrder(member, orderItems);
        //생성한 주문 엔티티 저장
        orderRepository.save(order);
        
        return order.getId();
        
    }

    /* 주문 목록을 조회 */
    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String email, Pageable pageable) {

        //유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        List<Order> orderList = orderRepository.findAllOrdersByEmail(email, pageable);
        //유저의 주문 총 개수를 계산
        Long totalCount = orderRepository.countOrderByEmail(email);

        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();

        //주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO를 생성
        for (Order order : orderList) {
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItemList = order.getOrderItems();
            for (OrderItem orderItem : orderItemList) {
                //주문한 상품의 대표 이미지를 조회
                ItemImg itemImg = itemImgRepository.findItemImgByIdAndRepimgYn(
                        orderItem.getItem().getId(), "Y");
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl());
                orderHistDTO.addOrderItemDTO(orderItemDTO);
            }
            orderHistDTOList.add(orderHistDTO);
        }
        //페이지 구현 객체를 생성하여 반환
        return new PageImpl<OrderHistDTO>(orderHistDTOList, pageable, totalCount);
    }


}
