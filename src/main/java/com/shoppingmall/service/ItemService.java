package com.shoppingmall.service;

import com.shoppingmall.dto.ItemDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.ItemImg;
import com.shoppingmall.repository.ItemImgRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품을 등록하는 로직을 가지는 service 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService imgService;
    private final ItemImgRepository imgRepository;


    public Long saveItem(ItemDTO itemDTO,
                         List<MultipartFile> imgFileList) throws Exception {

        /*
         상품등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체 생성
         */
        Item item = itemDTO.createItem();
        //상품 데이터 저장
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < imgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            //첫 번째 이미지일경우 대표 상품 이미지 여부 값을 Y 로 적용 나머지는 N 으로 적용
            if (i == 0) {
                itemImg.setRepimgYn("Y");
            }
            else {
                itemImg.setRepimgYn("N");
            }
            //상품 이미지 정보 저장
            imgService.saveItemImg(itemImg, imgFileList.get(i));
        }
        return item.getId();
    }


}
