package com.example.nanuri.domain.lesson.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant,ParticipantId> {
    @Modifying
    @Query(
            value = "select * from participant where LESSON_ID = :lessonId" , nativeQuery = true
    )
    List<Participant> findByLessonId(@Param("lessonId") Long lessonId);
}
