package com.example.nanuri.domain.lesson;

import com.example.nanuri.domain.BaseTimeEntity;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Lesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonId;

    @Column(nullable = false,length = 20)
    private String creator;

    @Column(nullable = false,length = 30)
    private String lessonName;

    @Column
    private String category;

    @Column
    private String location;

    @Column
    private int limitedNumber;

    @Column(length = 500)
    private String content;

    @Column
    @ColumnDefault("true")
    private Boolean status;

    @OneToMany(fetch = FetchType.EAGER )
    @JoinColumn(name = "lessonId")
    private List<LessonImg> images;

    @Builder
    public Lesson(String creator, String lessonName, String category, String location, int limitedNumber, String content) {
        this.creator = creator;
        this.lessonName = lessonName;
        this.category = category;
        this.location = location;
        this.limitedNumber = limitedNumber;
        this.content = content;
    }

    public void updateStatus(boolean updatedStatus){
        this.status = updatedStatus;
    }
}
