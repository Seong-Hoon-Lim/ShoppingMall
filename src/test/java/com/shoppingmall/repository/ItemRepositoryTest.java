package com.shoppingmall.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.thymeleaf.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;   //영속성 컨텍스트를 사용하기 위해 EntityManager Bean을 주입

    @Test
    @DisplayName("상품 저장 테스트")
    public void saveItemTest() {
        Item item = new Item();
        item.setName("상품1");
        item.setPrice(20000);
        item.setDescription("상품1 상세 설명");
        item.setItemStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
    }

    @Test
    @DisplayName("상품 리스트 저장 테스트")
    public void saveItemListTest() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setName("상품" + i);
            item.setPrice(20000 + i);
            item.setDescription("상품" + i + " 상세 설명");
            item.setItemStatus(ItemSellStatus.SELL);
            item.setStock(100);
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findItemByNameTest() {
        this.saveItemListTest();
        List<Item> itemList = itemRepository.findItemByName("상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명 or 상품상세설명 조회 테스트")
    public void findItemByNameOrDescriptionTest() {
        this.saveItemListTest();
        List<Item> itemList =
                itemRepository.findItemByNameOrDescription("상품2", "상품5 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findItemByPriceLessThanTest() {
        this.saveItemListTest();
        List<Item> itemList = itemRepository.findItemByPriceLessThan(20500);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findItemByPriceLessThanOrderByPriceDescTest() {
        this.saveItemListTest();
        List<Item> itemList = itemRepository.findItemByPriceLessThan(20500);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findItemByDescriptionTest() {
        this.saveItemListTest();
        List<Item> itemList = itemRepository.findItemByDescription("상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findItemByDescriptionByNativeTest() {
        this.saveItemListTest();
        List<Item> itemList = itemRepository.findItemByDescriptionByNative("상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.saveItemListTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); //JPAQueryFactory를 이용한 동적쿼리 생성 , em 객체 전달
        QItem qItem = QItem.item;   //Querydsl을 통해 쿼리를 생성하기 위해 자동으로 생성된 QItem 객체를 이용
        /*
         Java 코드로 SQL문 처럼 작성
         */
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.status.eq(ItemSellStatus.SELL))
                .where(qItem.description.like("%" + "상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    //JPAQuery 메소드인 fetch() 를 이용해서 쿼리 결과를 리스트로 반환

        for (Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    public void saveItemList() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setName("테스트상품" + i);
            item.setPrice(20000 + i);
            item.setDescription("테스트상품 상세설명" + i);
            item.setItemStatus(ItemSellStatus.SELL);
            item.setStock(100);
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setName("테스트상품" + i);
            item.setPrice(20000 + i);
            item.setDescription("테스트상품 상세설명" + i);
            item.setItemStatus(ItemSellStatus.SOLD_OUT);
            item.setStock(0);
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        this.saveItemList();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String description = "테스트상품 상세설명";
        int price = 20003;
        String status = "SELL";

        booleanBuilder.and(item.description.like("%" + description + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(status, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.status.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = (Pageable) PageRequest.of(0, 5);
        Page<Item> itemPagingResult =
                itemRepository.findAll((Predicate) booleanBuilder, pageable);
        System.out.println("total elements : " +
                            itemPagingResult.getTotalElements());

        List<Item> itemListResult = itemPagingResult.getContent();
        for (Item itemResult : itemListResult) {
            System.out.println(itemResult.toString());
        }

    }

}