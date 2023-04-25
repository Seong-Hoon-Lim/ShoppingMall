package com.shoppingmall.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * BaseTimeEntity 엔티티를 상속 받고 있으며
 * 등록일, 수정일, 등록자, 수정자를 모두 갖는 엔티티 클래스
 */
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    private String registeredBy;

    @LastModifiedBy
    private String updatedBy;

}
