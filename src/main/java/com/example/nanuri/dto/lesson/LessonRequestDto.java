package com.example.nanuri.dto.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@Getter
public class LessonRequestDto {

    private String lessonName;
    private String category;
    private String location;
    private int limitedNumber;
    private String content;
    private List<MultipartFile> images;

    @Override
    public String toString() {
        return "LessonRequestDto{" +
                "lessonName='" + lessonName + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", limitedNumber=" + limitedNumber +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }

    public Lesson toEntity(Long creator){
        return Lesson.builder()
                .creator(creator)
                .lessonName(lessonName)
                .category(category)
                .location(location)
                .limitedNumber(limitedNumber)
                .content(content)
                .build();
    }
}
