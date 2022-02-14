package com.example.nanuri.dto.lesson;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonParticipantResponseDto {

    private Long lessonId;

    private Long userId;

    @Builder
    public LessonParticipantResponseDto(Long lessonId, Long userId) {
        this.lessonId = lessonId;
        this.userId = userId;
    }
}
