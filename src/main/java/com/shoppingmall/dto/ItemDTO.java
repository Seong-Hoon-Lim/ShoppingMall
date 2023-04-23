package com.shoppingmall.dto;

import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * view 영역에서 사용할 클래스
 * 데이터를 주고 받을 때는 entity 자체를 반환하지 않고
 * Data Transfer Object DTO를 활용해야 함.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String description;

    private ItemSellStatus itemStatus;

    //상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDTO> itemImgList = new ArrayList<>();

    /*
     상품의 이미지 아이디를 저장하는 리스트
     상품 등록시에는 아직 상품의 이미지를 저장하지 않았기 때문에
     아무값도 들어가 있지 않음. 수정 시 이미지 아이디를 담을 용도
     */
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     createItem() / of(Item item) 메소드는 modelMapper 를 활용하여
     엔티티 객체와 DTO 객체간의 데이터를 복사하여 복사한 객체를 반환 해주는 메소드.
     */
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemDTO of(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }


}
