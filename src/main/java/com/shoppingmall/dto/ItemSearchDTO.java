package com.shoppingmall.dto;

import com.shoppingmall.constant.ItemSellStatus;
import lombok.*;

/**
 * 상품 데이터 조회 시 상품 조회 조건을 가지고 있는 클래스
 */
@Data
@Builder
@ToString
@Getter @Setter
public class ItemSearchDTO {

    /*
     현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회.
     조회 시간 기준
      - all: 상품 등록일 전체
      - 1d: 최근 하루 동안 등록된 상품
      - 1w: 최근 일주일 동안 등록된 상품
      - 1m: 최근 한달 동안 등록된 상품
      - 6m: 최근 6개월 동안 등록된 상품
     */
    private String searchDateType;

    //상품 판매상태를 기준으로 상품 데이터를 조회
    private ItemSellStatus searchSellStatus;

    /*
     상품을 조회할 때 어떤 유형으로 조회할지 선택
      - name: 상품명
      - registeredWorker: 상품 등록자 아이디
     */
    private String searchBy;

    /*
     조회할 검색어 저장할 변수.
     searchBy 가 name 일 경우 상품명 기준으로 검색
     registeredWorker 기준일 경우 상품 등록자 아이디 기준으로 검색
     */
    private String searchQuery = "";

}
