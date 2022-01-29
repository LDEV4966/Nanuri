package com.example.nanuri.domain.lesson;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column
    private String title;

    @Column
    private String description;

    @Builder
    public Lesson(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
