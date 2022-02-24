package com.example.nanuri.dto.lesson;

import com.example.nanuri.dto.user.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonRegistrationResponseDto {

    private Long lessonId;

    private UserResponseDto user;

    private String registrationForm;

    @Builder
    public LessonRegistrationResponseDto(Long lessonId,UserResponseDto user,String registrationForm) {
        this.lessonId = lessonId;
        this.user = user;
        this.registrationForm = registrationForm;
    }
}
