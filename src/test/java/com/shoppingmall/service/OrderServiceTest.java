package com.shoppingmall.service;

import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.dto.OrderDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.Member;
import com.shoppingmall.entity.Order;
import com.shoppingmall.entity.OrderItem;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.MemberRepository;
import com.shoppingmall.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;

    //테스트 상품 저장
    public Item saveItem() {
        Item item = new Item();
        item.setItemName("test상품");
        item.setPrice(19800);
        item.setDescription("test상품 입니다.");
        item.setItemStatus(ItemSellStatus.SELL);
        item.setStock(100);
        return itemRepository.save(item);
    }

    //테스트 회원 이메일 저장
    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test02@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void orderTest() {

        Item item = saveItem();
        Member member = saveMember();

        OrderDTO orderDTO = new OrderDTO();
        //주문 상품과 상품 수량을 orderDTO 객체에 세팅
        orderDTO.setCount(10);
        orderDTO.setItemId(item.getId());

        //주문 로직 호출 결과 생성된 주문 번호를 orderId 변수에 저장
        Long orderId = orderService.order(orderDTO, member.getEmail());

        //주문 번호를 이용해서 저장된 주문 정보를 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        //주문한 상품의 총 가격을 구함.
        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDTO.getCount() * item.getPrice();

        //주문한 상품의 총 가격과 DB에 저장된 상품의 가격을 비교
        assertEquals(totalPrice, order.getTotalPrice());
    }

}