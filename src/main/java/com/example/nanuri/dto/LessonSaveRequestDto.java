package com.example.nanuri.dto;

import com.example.nanuri.domain.lesson.Lesson;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonSaveRequestDto {
    private String title;
    private String description;

    @Builder
    public LessonSaveRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }


}
