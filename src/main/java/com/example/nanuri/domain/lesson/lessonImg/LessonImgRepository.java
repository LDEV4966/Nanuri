package com.example.nanuri.domain.lesson.lessonImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonImgRepository extends JpaRepository<LessonImg,LessonImgId> {

    //벌크 연산 @Modifying을 명시해줘야 한다.
    //기존 벌크 연산처럼 영향받은 엔티티의 개수를 반환한다.
    //	알다시피 벌크 연산은 영속성 컨텍스트를 무시한다.
    //	벌크 연산후에 영속성 컨텍스트를 초기화하고 싶으면 clearAutomatically 옵션을 true로 주면 된다. 기본값은 false이다
    @Modifying
    @Query(
            value = "delete from lesson_img where LESSON_ID = :lessonId" , nativeQuery = true
    )
    void deleteAllByLessonId(@Param("lessonId") Long lessonId);

    @Modifying
    @Query(
            value = "select * from lesson_img where LESSON_ID = :lessonId" , nativeQuery = true
    )
    List<LessonImg> findByLessonId(Long lessonId);
}
