package com.shoppingmall.repository;

import com.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository 를 상속 받아 기본적인 CRUD 및 페이징 처리 관련
 * 기본 메소드가 정의 되어 있음.
 *
 * <S extend T> save(S entity) : 엔티티 저장 및  수정
 * void delete(T entity) : 엔티티 삭제
 * count() : 엔티티 총 갯수 반환
 * Iterable<T> findAll() : 모든 엔티티 조회
 *
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
