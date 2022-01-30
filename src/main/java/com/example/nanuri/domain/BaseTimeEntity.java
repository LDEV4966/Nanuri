package com.example.nanuri.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity classe들이 BaseTimeEntity를 상속 할 경우 필드들도 칼럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class)// BaseTimeEntity 클래스에 Auditing 기능을 포함 시킵니다. Spring Data Jpa가 제공하는 엔티티의 변화를 추적하는 Audit 기능
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createDate;
}
