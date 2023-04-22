package com.shoppingmall.dto;

import com.shoppingmall.entity.ItemImg;
import lombok.*;
import org.modelmapper.ModelMapper;

/**
 * 상품 저장 후 상품 이미지에 대한 데이터를 전달 할
 * DTO 클래스
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImgDTO {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    /*
     ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과
     멤버변수의 이름이 같을 때 ItemImgDTO로 값을 복사해서 반환
     static 메소드로 선언해서 ItemImgDTO 객체를 생성하지 않아도 호출 가능.
     */
    public static ItemImgDTO of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }

}
