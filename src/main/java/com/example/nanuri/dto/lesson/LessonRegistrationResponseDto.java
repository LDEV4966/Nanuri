package com.example.nanuri.dto.lesson;

import com.example.nanuri.domain.lesson.registration.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonRegistrationResponseDto {

    private Long lessonId;

    private Long userId;

    private RegistrationStatus status;

    private String registrationForm;

    @Builder
    public LessonRegistrationResponseDto(Long lessonId, Long userId, RegistrationStatus status, String registrationForm) {
        this.lessonId = lessonId;
        this.userId = userId;
        this.status = status;
        this.registrationForm = registrationForm;
    }
}
