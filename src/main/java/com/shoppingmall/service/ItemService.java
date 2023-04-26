package com.shoppingmall.service;

import com.shoppingmall.dto.ItemDTO;
import com.shoppingmall.dto.ItemImgDTO;
import com.shoppingmall.dto.ItemSearchDTO;
import com.shoppingmall.dto.MainItemDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.ItemImg;
import com.shoppingmall.repository.ItemImgRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    /*
     등록된 상품을 불러오는 메소드
     @Transactional(readOnly = true): 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정
     이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아 성능 향상 시킬 수 있음.
     */
    @Transactional(readOnly = true)
    public ItemDTO findItemInfo(Long itemId) {

        List<ItemImg> imgList =
                //해당되는 상품의 이미지를 조회. 등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순 조회
                imgRepository.findItemImgByItemIdOrderByIdAsc(itemId);
        List<ItemImgDTO> imgDTOList = new ArrayList<>();
        
        //조회한 ItemImg 엔티티를 ItemImgDTO 객체로 만들어서 리스트에 추가
        for (ItemImg itemImg : imgList) {
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
            imgDTOList.add(itemImgDTO);
        }
        
        //상품의 아이디를 통해 상품 엔티티를 조회. 존재하지 않을 때는 EntityNotFoundException 발생
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemDTO itemDTO = ItemDTO.of(item);
        itemDTO.setItemImgList(imgDTOList);
        return itemDTO;
    }

    /* 상품 수정 메소드 */
    public Long updateItem(ItemDTO itemDTO,
                           List<MultipartFile> imgFileList) throws Exception {

        //상품 등록 화면에서 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        Item item = itemRepository.findById(itemDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        //상품 등록 화면에서 전달 받은 itemDTO 객체를 통해 상품 엔티티를 업데이트
        item.updateItem(itemDTO);

        //상품 이미지 아이디 리스트를 조회
        List<Long> itemImgIds = itemDTO.getItemImgIds();

        //상품 이미지를 업데이트하기 위해서 상품 이미지, 아이디와, 상품 이미지 파일 정보를 넘겨줌
        for (int i = 0; i < imgFileList.size(); i++) {
            imgService.updateItemImg(itemImgIds.get(i), imgFileList.get(i));
        }
        return item.getId();

    }

    /*
     상품 관리 페이지 목록을 불러오는 메소드 상품 조회 조건 ItemSearchDTO 과 페이지 정보를
     파라미터로 받아서 상품 데이터를 조회하는 메소드 getAdminItemPage() 를 추가.
     데이터 수정이 일어나지 않으므로 @Transactional(readOnly = true) 최적화 위해 설정.
     */
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO,
                                       Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDTO, pageable);
    }

    /* 메인 페이지에 상품 데이터를 보여주기 위한 메소드 */
    @Transactional(readOnly = true)
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO searchDTO,
                                             Pageable pageable) {
        return itemRepository.getMainItemPage(searchDTO, pageable);
    }


}
