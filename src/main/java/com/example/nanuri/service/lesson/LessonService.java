package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.dto.LessonRequestDto;
import com.example.nanuri.dto.LessonSaveRequestDto;
import com.example.nanuri.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final S3Service s3Service;

    @Transactional
    public void save(LessonRequestDto lessonRequestDto) throws IOException {
        lessonRepository.save(lessonRequestDto.toEntity());
        for(MultipartFile multipartFile : lessonRequestDto.getImages()){
            System.out.println(s3Service.upload(multipartFile, "lessonImg"));
        }
    }
}
