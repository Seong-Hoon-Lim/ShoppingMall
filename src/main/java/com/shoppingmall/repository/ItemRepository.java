package com.shoppingmall.repository;

import com.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    /**
      상품 이름을 이용하여 데이터를 조회하는 기능
      쿼리 메소드 사용 시 반드시 정해진 규칙 준수!
      find + entity클래스명 + By + 조회할 필드명
     */
    List<Item> findItemByName(String name);

    /**
     * 상품명과 상품 상세 설명을 OR 조건을 이용하여
     * 조회하는 쿼리 메소드
     * find + entity클래스명 + By + 조회할 필드명 + Or + 조회할 필드명
     */
    List<Item> findItemByNameOrDescription(String name, String description);

    /**
     * 파라미터로 넘어온 price 변수보다 값이 작은 데이터를
     * 조회하는 쿼리 메소드
     */
    List<Item> findItemByPriceLessThan(Integer price);

    /**
     * 상품의 가격이 높은 순으로 내림차순으로 데이터를
     * 조회하는 메소드
     */
    List<Item> findItemByPriceLessThanOrderByPriceDesc(Integer price);

    /**
     * @Query 어노테이션을 활용하여 JPQL 쿼리문 사용
     * 상품 상세 설명을 파라미터로 받아 해당 내용을
     * 상품 상세 설명에 포함하고 있는 데이터를 조회
     * 정렬 순서는 가격이 높은 순으로 조회하는 메소드
     *
     * JPQL에서 LIKE 연산자를 사용하는 경우 반드시
     * % % 기호를 문자열 값과 함께 CONCAT() 함수로 감싸서 사용.
     */
    @Query("SELECT i FROM Item i WHERE i.description LIKE CONCAT('%', :description, '%') ORDER BY i.price DESC")
    List<Item> findItemByDescription(@Param("description") String description);

    /**
     * 기존의 데이터베이스에서 사용하던 쿼리를 그대로 사용하는 경우
     * @Query 의 nativeQuery 속성을 사용하면 기존의 쿼리를 그대로 사용 가능
     * 특정 데이터베이스에 종속적이게 되므로 JPA 의 장점을 잃게 됨
     */
    @Query(value = "SELECT * FROM item i WHERE i.description LIKE CONCAT('%', :description, '%') ORDER BY i.price DESC", nativeQuery = true)
    List<Item> findItemByDescriptionByNative(@Param("description") String description);

}
