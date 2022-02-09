package com.example.nanuri.dto.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class LessonResponseDto {

    private Long lessonId;

    private Long creator;

    private String lessonName;

    private String category;

    private String location;

    private int limitedNumber;

    private String content;

    private LocalDateTime createDate;

    private Boolean status;

    private List<LessonImg> images;

    public LessonResponseDto(Lesson entity){
        this.lessonId = entity.getLessonId();
        this.creator = entity.getCreator();
        this.lessonName = entity.getLessonName();
        this.category = entity.getCategory();
        this.location = entity.getLocation();
        this.limitedNumber = entity.getLimitedNumber();
        this.content = entity.getContent();
        this.createDate = entity.getCreateDate();
        this.status =entity.getStatus();
        this.images = entity.getImages();
    }

}
