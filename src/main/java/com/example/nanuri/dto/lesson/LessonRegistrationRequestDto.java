package com.example.nanuri.dto.lesson;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonRegistrationRequestDto {
    private Long lessonId;
    private Long userId;
    private String registerForm;

    @Builder
    public LessonRegistrationRequestDto(Long lessonId, Long userId, String registerForm) {
        this.lessonId = lessonId;
        this.userId = userId;
        this.registerForm = registerForm;
    }

    @Override
    public String toString() {
        return "LessonRegistrationRequestDto{" +
                "lessonId=" + lessonId +
                ", userId=" + userId +
                ", registerForm='" + registerForm + '\'' +
                '}';
    }
}
