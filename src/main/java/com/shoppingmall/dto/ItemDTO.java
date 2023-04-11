package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * view 영역에서 사용할 클래스
 * 데이터를 주고 받을 때는 entity 자체를 반환하지 않고
 * Data Transfer Object DTO를 활용해야 함.
 */
@Getter
@Setter
public class ItemDTO {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String sellStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
