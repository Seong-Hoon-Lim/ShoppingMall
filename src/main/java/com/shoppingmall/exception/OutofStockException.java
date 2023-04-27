package com.shoppingmall.exception;

/**
 * 상품의 주문 수량보다 재고의 수가 적을 때 발생시킬
 * exception 을 정의하는 클래스
 */
public class OutofStockException extends RuntimeException {

    public OutofStockException(String message) {
        super(message);
    }

}
