package com.example.nanuri.domain.lesson.lessonImg;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class LessonImgId implements Serializable {

    @Column(name = "lessonId")
    private Long lessonId;

    @Column (name = "lessonImg")
    private String lessonImg;

    @Builder
    public LessonImgId(Long lessonId, String lessonImg) {
        this.lessonId = lessonId;
        this.lessonImg = lessonImg;
    }
}
