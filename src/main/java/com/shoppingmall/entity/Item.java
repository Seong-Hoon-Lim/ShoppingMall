package com.shoppingmall.entity;

import com.shoppingmall.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
//@Getter
//@Setter
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 생성을 DBMS 에 위임 (MySQL 경우 AI 적용됨)
    private Long id;    //상품코드

    @Column(name = "item_name", nullable = false, length = 50)  // VARCHAR(50) NOT NULL
    private String name;   //상품명

    @Column(nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stock;   //재고수량

    @Lob //대용량 파일 또는 이미지, 사운드, 비디오 같은 데이터를 DB 외부에 저장하기 위한 타입
    @Column(nullable = false)
    private String description; //상품 상세 설명

    @Enumerated(EnumType.STRING)//String 타입의 enum 타입을 매핑
    @Column(name = "item_status")
    private ItemSellStatus status; //상품 판매 상태

    private LocalDateTime createTime;   //등록 시간

    private LocalDateTime updateTime;  //수정 시간

    /*
     Q클래스 활용 시 lombok의 어노테이션을 활용한 getter, setter 메소드는
     컴파일 되지 않으므로 직접 메소드 생성 필요
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemSellStatus getStatus() {
        return status;
    }

    public void setStatus(ItemSellStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
