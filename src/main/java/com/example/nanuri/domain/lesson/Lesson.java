package com.example.nanuri.domain.lesson;

import com.example.nanuri.domain.BaseTimeEntity;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Lesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column
    private String title;

    @Column
    private String description;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "lessonId")
    private List<LessonImg> lessonImgs;

    @Builder
    public Lesson(String title, String description)  {
        this.title = title;
        this.description = description;
    }
}
