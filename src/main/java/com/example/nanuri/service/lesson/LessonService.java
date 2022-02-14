package com.example.nanuri.service.lesson;

import com.example.nanuri.auth.jwt.JwtTokenProvider;
import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.dto.lesson.LessonRegistrationRequestDto;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.handler.exception.AuthenticationForbiddenException;
import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.LessonNotFoundException;
import com.example.nanuri.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonImgRepository lessonImgRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void save(LessonRequestDto lessonRequestDto , Authentication authentication) {
        Lesson lesson  = lessonRepository.save(lessonRequestDto.toEntity(Long.parseLong(authentication.getName())));
        if(lessonRequestDto.getImages()!=null) {
            System.out.println(lessonRequestDto.getImages());
            System.out.println(lessonRequestDto.getImages().isEmpty());
            System.out.println(lessonRequestDto.getImages().size());
            for (MultipartFile multipartFile : lessonRequestDto.getImages()) {
                System.out.println(multipartFile);
                String lessonImg = s3Service.upload(multipartFile, "lessonImg");
                lessonImgRepository.save(
                        LessonImg.builder()
                                .lessonImgId(
                                        LessonImgId.builder()
                                                .lessonId(lesson.getLessonId())
                                                .lessonImg(lessonImg)
                                                .build())
                                .build());
            }
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
    public List<LessonResponseDto> findByCreator(Long userId){
        return lessonRepository.findByCreator(userId).stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LessonResponseDto findById(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));
        return new LessonResponseDto(lesson);
    }

    @Transactional
    public void delete(Long lessonId , Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson의 생성자가 api 요청자와 동일한지 확인
        if(!isAuthorizedUser(lesson.getCreator(),authentication)){
            return;
        }

        // lesson 이미지 찾기
        List<LessonImg> lessonImgs = lessonImgRepository.findByLessonId(lessonId);

        //S3에서 이미지 파일 삭제
        for(LessonImg lessonImg : lessonImgs){
            s3Service.deleteImage(lessonImg.getLessonImgId().getLessonImg());
        }

        //DB 에 삭제
        lessonImgRepository.deleteAllByLessonId(lessonId);
        lessonRepository.delete(lesson);

    }

    @Transactional
    public void updateStatus(Long lessonId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson의 생성자가 api 요청자와 동일한지 확인
        if(!isAuthorizedUser(lesson.getCreator(),authentication)){
            return;
        }

        lesson.updateStatus();
    }

    @Transactional
    public void saveRegistrationInfo(Long lessonId, Authentication authentication, LessonRegistrationRequestDto lessonRegistrationRequestDto){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson의 생성자가 api 요청자와 동일한지 확인
        if(!isAuthorizedUser(lesson.getCreator(),authentication)){
            return;
        }

        Long userId = Long.parseLong(authentication.getName());

        System.out.println(lessonRegistrationRequestDto.toString());

        return;

    }

    private boolean isAuthorizedUser(Long creatorId ,Authentication authentication){
        if(authentication==null){
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        if( Long.parseLong(authentication.getName()) != creatorId){
            throw new AuthenticationForbiddenException(ErrorCode.FORBIDDEN_AUTHENTICATION);
        }
        return true;
    }

}
