package com.shoppingmall.repository;

import com.shoppingmall.dto.ItemSearchDTO;
import com.shoppingmall.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Querydsl을 Spring Data Jpa 와 함께 사용하기 위해서는
 * 사용자 정의 레포지토리 정의 필요
 * 
 *  1. 사용자 정의 인터페이스 작성
 *  2. 사용자 정의 인터페이스 구현
 *  3. Spring Data Jpa 레포지토리에서 사용자 정의 인터페이스 상속
 */
public interface ItemRepositoryCustom {

    /*
     상품 조회 조건을 담고 있는 itemSearchDTO 객체와 페이징 정보를 담고 있는
     pageable 객체를 파라미터로 받는 getAdminItemPage 메소드 정의
     반환 데이터로 Page<Item> 객체를 반환
     */
    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

}
