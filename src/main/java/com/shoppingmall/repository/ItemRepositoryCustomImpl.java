package com.shoppingmall.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.constant.ItemSellStatus;
import com.shoppingmall.dto.ItemSearchDTO;
import com.shoppingmall.entity.Item;
import com.shoppingmall.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    //동적으로 쿼리를 생성하기 위해 JPAQyeryFactory 클래스를 사용
    private JPAQueryFactory queryFactory;

    //JPAQueryFactory의 생성자로 EntityManager 객체를 넣어줌
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*
     상품 판매 상태 조건이 전체 null일 경우는 null을 리턴
     결과값이 null 이면 where절에서 해당 조건은 무시됨.
     상품 판매 상태 조건이 null이 아니라 판매중 or 품절 상태라면
     해당 조건의 상품만 조회
     */
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus ==
                null ? null : QItem.item.itemStatus.eq(searchSellStatus);
    }

    /*
     searchDateType의 값에 따라서 dateTime의 값을 이전 시간의 값으로
     세팅 후 해당 시간 이후로 등록된 상품만 조회함.
     예를 들어 searchDateType의 값이 1m인 경우 dateTime의 시간을 한 달
     전으로 세팅 후 최근 한 달 동안 등록된 상품만 조회하도록 조건값 반환.
     */
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        }
        else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        }
        else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        }
        else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        }
        else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.registeredTime.after(dateTime);
    }

    /*
     searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의
     아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환함.
     */
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        }
        else if (StringUtils.equals("registeredWorker", searchBy)) {
            return QItem.item.registeredWorker.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        /*
         Querydsl 시작
         queryFactory를 이용해서 쿼리를 생성.
         selectFrom(QItem, item): 상품 데이터를 조회하기 위해 QItem의 item 지정
         where 조건절: BooleanExpression 반환하는 조건문을 넣어줌
            '.' 단위로 넣어주면 and 조건으로 인식
         offset: 데이터를 가지고 올 시작 인덱스를 지정
         limit: 한 번에 가지고 올 최대 개수를 지정.
         fetchResult(): 조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환
         상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 쿼리문 실행

         */
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDTO.getSearchDateType()),
                searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
        searchByLike(itemSearchDTO.getSearchBy(),
        itemSearchDTO.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        //조회한 데이터를 Page 클래스의 구현체 PageImpl 객체로 반환
        return new PageImpl<>(content, pageable, total);
    }
}
