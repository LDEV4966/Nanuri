package com.example.nanuri.dto.lesson;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LessonRegistrationRequestDto {

    private String registrationForm;

    @Builder
    public LessonRegistrationRequestDto(String registrationForm) {
        this.registrationForm = registrationForm;
    }
}
