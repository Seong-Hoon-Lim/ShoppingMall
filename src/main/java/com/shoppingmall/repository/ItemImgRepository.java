package com.shoppingmall.repository;

import com.shoppingmall.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 상품의 이미지 정보를 저장하기 위한 인터페이스
 */
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    /*
     상품 이미지 아이디의 오름차순으로 DB를 조회하는 쿼리 메소드로
     상품 아이디를 매개변수로 가짐
     */
    List<ItemImg> findItemImgsByItemIdOrderByIdAsc(Long itemId);

}
