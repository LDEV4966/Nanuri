package com.example.nanuri.dto;

import com.example.nanuri.domain.lesson.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@Getter
public class LessonRequestDto {

    private String title;
    private String description;
    private List<MultipartFile> images;

    @Override
    public String toString() {
        return "LessonRequestDto{" +
                "image=" + images +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Lesson toEntity(){
        return Lesson.builder()
                .title(title)
                .description(description)
                .build();
    }
}
