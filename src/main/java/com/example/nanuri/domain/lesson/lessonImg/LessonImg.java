package com.example.nanuri.domain.lesson.lessonImg;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity
public class LessonImg {

    @EmbeddedId
    private LessonImgId lessonImgId;

    @Builder
    public LessonImg(LessonImgId lessonImgId) {
        this.lessonImgId = lessonImgId;
    }
}
