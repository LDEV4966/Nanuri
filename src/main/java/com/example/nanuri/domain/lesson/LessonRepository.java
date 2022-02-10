package com.example.nanuri.domain.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Modifying
    @Query(
            value = "select * from lesson where location = :location" , nativeQuery = true
    )
    List<Lesson> findByLocation(@Param("location") String location);

    @Modifying
    @Query(
            value = "select * from lesson where creator = :creator" , nativeQuery = true
    )
    List<Lesson> findByCreator(@Param("creator") Long creator);
}
