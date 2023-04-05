package com.shoppingmall.entity;

import com.shoppingmall.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 생성을 DBMS 에 위임 (MySQL 경우 AI 적용됨)
    private Long item_id;    //상품코드

    @Column(nullable = false, length = 50)  // VARCHAR(50) NOT NULL
    private String item_name;   //상품명

    @Column(nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stock_number;   //재고수량

    @Lob //대용량 파일 또는 이미지, 사운드, 비디오 같은 데이터를 DB 외부에 저장하기 위한 타입
    @Column(nullable = false)
    private String item_detail; //상품 상세 설명

    @Enumerated(EnumType.STRING)    //String 타입의 enum 타입을 매핑
    private ItemSellStatus item_status; //상품 판매 상태

    private LocalDateTime create_time;   //등록 시간

    private LocalDateTime update_time;  //수정 시간

}
