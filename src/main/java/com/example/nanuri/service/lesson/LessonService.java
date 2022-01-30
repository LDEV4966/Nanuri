package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonImgRepository lessonImgRepository;
    private final S3Service s3Service;

    @Transactional
    public void save(LessonRequestDto lessonRequestDto) throws IOException {
        int lessonId  = lessonRepository.save(lessonRequestDto.toEntity()).getLessonId();
        if(lessonRequestDto.getImages()==null)
            return;
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

    @Transactional(readOnly = true)
    public List<LessonResponseDto> findAll(){
        return lessonRepository.findAll().stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> findByLocation(String location){
        return lessonRepository.findByLocation(location).stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LessonResponseDto findById(int lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new IllegalArgumentException("해당 레슨이 없습니다. lessonId = "+lessonId) );
        return new LessonResponseDto(lesson);
    }


    @Transactional
    public void delete(int lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new IllegalArgumentException("해당 레슨이 없습니다. lessonId = "+lessonId));
        lessonImgRepository.deleteAllByLessonId(lessonId);
        lessonRepository.delete(lesson);
    }

}
