package com.shoppingmall.entity;

import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.MemberRepository;
import com.shoppingmall.repository.OrderItemRepository;
import com.shoppingmall.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {

        Item item = new Item();
        item.setName("테스트 셔츠");
        item.setPrice(20000);
        item.setDescription("테스트 셔츠입니다");
        item.setItemStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000);
            orderItem.setOrder(order);
            //아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order 엔티티에 담음
            order.getOrderItems().add(orderItem);
        }

        //order 엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 DB에 반영
        orderRepository.saveAndFlush(order);
        em.clear(); //영속성의 컨텍스트의 상태 초기화

         /*
          영속성 컨텍스트를 초기화했기 때문에 DB에서 주문 엔티티를 조회
          select 쿼리문이 실행됨
          */
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    //주문 데이터를 생성해서 저장하는 메소드
    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    /*
     주문 엔티티(부모 엔티티)에서 주문 상품(자식 엔티티)를 삭제했을 때
     orderItem 엔티티가 삭제되는지 여부를 테스트
     */
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        //order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소 제거
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {

        //기존에 만들었던 주문 생성 메소드를 이용 주문 데이터 저장
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        /*
          영속성 컨텍스트의 상태 초기화 후 order 엔티티에 저장했던
          주문 상품 아이디를 이용 orderItem을 DB에서 조회
         */
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        /*
         orderItem 엔티티에 있는 order 객체의 클래스를 출력
         Order 클래스가 출력됨이 확인됨.
         */
        System.out.println("Order class : " + orderItem.getOrder().getClass());
    }



}