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
        item.setItem_name("상품1");
        item.setPrice(20000);
        item.setItem_detail("상품1 상세 설명");
        item.setItem_status(ItemSellStatus.SELL);
        item.setStock_number(100);
        item.setCreate_time(LocalDateTime.now());
        item.setUpdate_time(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        logger.info("save...");

    }
}