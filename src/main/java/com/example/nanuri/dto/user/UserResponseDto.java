package com.example.nanuri.dto.user;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.user.Role;
import com.example.nanuri.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserResponseDto {

    private Long userId;

    private String name;

    private String email;

    private String imageUrl;


    public UserResponseDto(User entity){
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.imageUrl = entity.getImageUrl();
    }
}
