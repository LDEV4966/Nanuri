package com.example.nanuri.dto.user;

import com.example.nanuri.dto.lesson.LessonResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserLessonResponseDto {
    private UserResponseDto user;
    private List<LessonResponseDto> lessonList;

    public UserLessonResponseDto(UserResponseDto userResponseDto,List<LessonResponseDto> lessonResponseDtos){
        this.user = userResponseDto;
        this.lessonList = lessonResponseDtos;
    }
}
