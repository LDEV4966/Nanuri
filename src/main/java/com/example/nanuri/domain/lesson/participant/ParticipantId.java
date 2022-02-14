package com.example.nanuri.domain.lesson.participant;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ParticipantId implements Serializable {

    private Long lessonId;

    private Long userId;

    @Builder
    public ParticipantId(Long lessonId, Long userId) {
        this.lessonId = lessonId;
        this.userId = userId;
    }
}
