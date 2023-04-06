package com.shoppingmall.repository;

import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.entity.Item;
import org.apache.logging.log4j.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    private static final Logger logger = LogManager.getLogger(ItemRepositoryTest.class);

    @Test
    @DisplayName("상품 저장 테스트")
    public void saveItemTest() {
        Item item = new Item();
        item.setName("상품1");
        item.setPrice(20000);
        item.setDescription("상품1 상세 설명");
        item.setStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        logger.info("save...");
    }

    @Test
    @DisplayName("상품 리스트 저장 테스트")
    public void saveItemListTest() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setName("상품" + i);
            item.setPrice(20000 + i);
            item.setDescription("상품" + i + " 상세 설명");
            item.setStatus(ItemSellStatus.SELL);
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

}