package com.example.nanuri.service.lesson;

import com.example.nanuri.auth.jwt.JwtTokenProvider;
import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.domain.lesson.registration.Registration;
import com.example.nanuri.domain.lesson.registration.RegistrationId;
import com.example.nanuri.domain.lesson.registration.RegistrationRepository;
import com.example.nanuri.domain.lesson.registration.RegistrationStatus;
import com.example.nanuri.dto.lesson.LessonRegistrationRequestDto;
import com.example.nanuri.dto.lesson.LessonRegistrationResponseDto;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.handler.exception.*;
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
    private final RegistrationRepository registrationRepository;
    private final S3Service s3Service;

    //레슨 저장
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

    // 모든 레슨 조회
    @Transactional(readOnly = true)
    public List<LessonResponseDto> findAll(){
        return lessonRepository.findAll().stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    //위치 기반으로 레슨 정보 조회
    @Transactional(readOnly = true)
    public List<LessonResponseDto> findByLocation(String location){
        return lessonRepository.findByLocation(location).stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    //생성자로 레슨정보 조회
    @Transactional(readOnly = true)
    public List<LessonResponseDto> findByCreator(Long userId){
        return lessonRepository.findByCreator(userId).stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    //레슨 아이디로 레슨 상세정보 조회
    @Transactional(readOnly = true)
    public LessonResponseDto findById(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));
        return new LessonResponseDto(lesson);
    }

    //레슨 삭제
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

    //레슨 모집 상태 업데이트
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

    //레슨 아이디를 기반으로 레슨 신청자 조회
    @Transactional(readOnly = true)
    public List<LessonRegistrationResponseDto> findLessonRegistrationInfoByLessonId(Long lessonId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson 생성자만 신청서를 조회 가능
        if(lesson.getCreator() != Long.parseLong(authentication.getName())){
            throw new AuthenticationForbiddenException(ErrorCode.FORBIDDEN_AUTHENTICATION);
        }

        List<Registration> registrations = registrationRepository.findByLessonId(lessonId);

        return registrations.stream()
                .map( registration ->
                        LessonRegistrationResponseDto
                                .builder()
                                .userId(registration.getRegistrationId().getUserId())
                                .lessonId(registration.getRegistrationId().getLessonId())
                                .status(registration.getStatus())
                                .registrationForm(registration.getRegistrationForm())
                                .build() )
                .collect(Collectors.toList());

    }

    //레슨 아이디와 로그인 된 사용자를 기반으로 레슨 신청
    @Transactional
    public void saveRegistrationInfo(Long lessonId, Authentication authentication, LessonRegistrationRequestDto lessonRegistrationRequestDto){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // 유저 아이디
        Long userId = Long.parseLong(authentication.getName());

        // Todo : lesson의 현재 수강인원과 수강정원 비교 및 레슨 모집상태 확인


        // DB 저장
        registrationRepository.save(
                Registration
                .builder()
                        .registrationId(RegistrationId.builder().lessonId(lessonId).userId(userId).build())
                        .registrationForm(lessonRegistrationRequestDto.getRegistrationForm())
                .build());
    }

    //레슨 신청허가
    @Transactional
    public void acceptLessonRegistration(Long lessonId,Long userId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson 생성자만 레슨 신청허가 가능
        if(lesson.getCreator() != Long.parseLong(authentication.getName())){
            throw new AuthenticationForbiddenException(ErrorCode.FORBIDDEN_AUTHENTICATION);
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        registration.updateRegistraionStatus(RegistrationStatus.ACCEPTED);

    }

    //레슨 신청거절
    @Transactional
    public void denyLessonRegistration(Long lessonId,Long userId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson 생성자만 레슨 신청거절 가능
        if(lesson.getCreator() != Long.parseLong(authentication.getName())){
            throw new AuthenticationForbiddenException(ErrorCode.FORBIDDEN_AUTHENTICATION);
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        registration.updateRegistraionStatus(RegistrationStatus.DENIED);

        // Todo : 신청자에게 거절 알림 기능
    }


    //생성자와 현 유저가 동일한 지 확인
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
