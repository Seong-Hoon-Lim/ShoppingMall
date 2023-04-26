package com.shoppingmall.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class MainItemDTO {

    private Long id;
    
    private String itemName;
    
    private String description;
    
    private String imgUrl;
    
    private Integer price;

    /*
     생성자에 @QueryProjection 어노테이션을 선언하여 Querydsl 로 결과 조회 시
     MainItemDTO 객체로 바로 받아도록 활용
     */
    @QueryProjection
    public MainItemDTO(Long id, String itemName, String description, 
                       String imgUrl, Integer price) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
