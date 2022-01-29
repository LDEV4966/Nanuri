package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.dto.LessonRequestDto;
import com.example.nanuri.dto.LessonSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    @Transactional
    public void save(LessonRequestDto lessonRequestDto){
        lessonRepository.save(lessonRequestDto.toEntity());

    }


}
