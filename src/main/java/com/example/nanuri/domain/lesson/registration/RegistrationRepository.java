package com.example.nanuri.domain.lesson.registration;

import com.example.nanuri.domain.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,RegistrationId> {

    @Modifying
    @Query(
            value = "select * from registration where LESSON_ID = :lessonId" , nativeQuery = true
    )
    List<Registration> findByLessonId(@Param("lessonId") Long lessonId);
}
