package com.shoppingmall.service;

import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.dto.ItemDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.ItemImg;
import com.shoppingmall.repository.ItemImgRepository;
import com.shoppingmall.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/upload/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName,
                            "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItemTest() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("테스트 신발");
        itemDTO.setItemStatus(ItemSellStatus.SELL);
        itemDTO.setDescription("테스트 신발 입니다.");
        itemDTO.setPrice(30000);
        itemDTO.setStock(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemDTO, multipartFileList);

        List<ItemImg> itemImgs =
                itemImgRepository.findItemImgsByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemDTO.getName(), item.getName());
        assertEquals(itemDTO.getItemStatus(), item.getItemStatus());
        assertEquals(itemDTO.getDescription(), item.getDescription());
        assertEquals(itemDTO.getPrice(), item.getPrice());
        assertEquals(itemDTO.getStock(), item.getStock());
        assertEquals(multipartFileList.get(0).getOriginalFilename(),
                itemImgs.get(0).getOriImgName());
    }

}