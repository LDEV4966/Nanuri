package com.example.nanuri.domain.lesson.registration;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RegistrationId implements Serializable {

    @Column(name = "lessonId")
    private Long lessonId;

    @Column (name = "userId")
    private Long userId;

    @Builder
    public RegistrationId(Long lessonId, Long userId) {
        this.lessonId = lessonId;
        this.userId = userId;
    }

}
