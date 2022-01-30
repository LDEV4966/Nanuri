package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.dto.LessonRequestDto;
import com.example.nanuri.dto.LessonSaveRequestDto;
import com.example.nanuri.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonImgRepository lessonImgRepository;
    private final S3Service s3Service;

    @Transactional
    public void save(LessonRequestDto lessonRequestDto) throws IOException {
        Long lessonId  = lessonRepository.save(lessonRequestDto.toEntity()).getLessonId();
        for(MultipartFile multipartFile : lessonRequestDto.getImages()){
            String lessonImg = s3Service.upload(multipartFile, "lessonImg");
            lessonImgRepository.save(
                    LessonImg.builder()
                            .lessonImgId(
                                    LessonImgId.builder()
                                            .lessonId(lessonId)
                                            .lessonImg(lessonImg)
                                            .build())
                            .build());
        }
    }

    @Transactional
    public List<Lesson> findAll(){
        return lessonRepository.findAll();
    }
}
