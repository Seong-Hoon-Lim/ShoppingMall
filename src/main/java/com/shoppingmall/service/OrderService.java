package com.shoppingmall.service;

import com.shoppingmall.dto.OrderDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.Member;
import com.shoppingmall.entity.Order;
import com.shoppingmall.entity.OrderItem;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.MemberRepository;
import com.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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


}
