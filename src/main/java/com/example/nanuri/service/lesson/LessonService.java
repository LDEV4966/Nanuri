package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.domain.lesson.participant.Participant;
import com.example.nanuri.domain.lesson.participant.ParticipantRepository;
import com.example.nanuri.domain.lesson.registration.Registration;
import com.example.nanuri.domain.lesson.registration.RegistrationRepository;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.handler.exception.AuthenticationNullPointerException;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.LessonNotFoundException;
import com.example.nanuri.handler.exception.UnAuthorizedUserException;
import com.example.nanuri.service.aws.S3Service;
import com.example.nanuri.service.lesson.participant.ParticipantService;
import com.example.nanuri.service.lesson.registration.RegistrationService;
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
    private final ParticipantRepository participantRepository;

    private final S3Service s3Service;
    private final ParticipantService participantService;
    private final RegistrationService registrationService;
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
    @Transactional(readOnly = true)
    public List<LessonResponseDto> findSubscribedLesson(Long userId){

        // 참여자 조회
        List<Long> lessonIds = participantRepository.findByUserId(userId);

        // 레슨 id 조회 후 레슨 조회
        List<Lesson> lessons = lessonIds.stream()
                .map(lessonId -> lessonRepository.findById(lessonId).orElseThrow(()->new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND)))
                .collect(Collectors.toList());

        // Dto 변환
        return lessons.stream()
                .map( lesson -> new LessonResponseDto(lesson))
                .collect(Collectors.toList());
    }

    //레슨 아이디로 레슨 상세정보 조회
    @Transactional(readOnly = true)
    public LessonResponseDto findById(Long lessonId,Authentication authentication){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        if(authentication==null){
            return new LessonResponseDto(lesson,participantService.findLessonParticipantCount(lesson.getLessonId()));
        }

        return new LessonResponseDto(lesson,participantService.findLessonParticipantCount(lesson.getLessonId()),registrationService.isRegistered(lessonId,authentication),participantService.isParticipant(lessonId,authentication));
    }

    //레슨 삭제
    @Transactional
    public void delete(Long lessonId , Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson의 생성자가 api 요청자와 동일한지 확인
        if(!isCreator(lesson.getCreator(),authentication)){
            return;
        }

        // lesson 이미지 찾기
        List<LessonImg> lessonImgs = lessonImgRepository.findByLessonId(lessonId);

        //S3에서 이미지 파일 삭제
        for(LessonImg lessonImg : lessonImgs){
            s3Service.deleteImage(lessonImg.getLessonImgId().getLessonImg());
        }

        // 레슨 관련 참여자 및 신청서 삭제를 위한 조회
        List<Participant> participants = participantRepository.findByLessonId(lessonId);
        List<Registration> registrations = registrationRepository.findByLessonId(lessonId);

        //DB 에 삭제0
        lessonImgRepository.deleteAllByLessonId(lessonId);
        participantRepository.deleteAll(participants);
        registrationRepository.deleteAll(registrations);
        lessonRepository.delete(lesson);

    }

    //레슨 모집 상태 업데이트
    @Transactional
    public void updateStatus(Long lessonId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson의 생성자가 api 요청자와 동일한지 확인
        if(!isCreator(lesson.getCreator(),authentication)){
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }

        lesson.updateStatus();
    }


    //생성자와 현 유저가 동일한 지 확인
    private boolean isCreator(Long creatorId ,Authentication authentication){
        if(authentication==null){
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        if( Long.parseLong(authentication.getName()) != creatorId){
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }
        return true;
    }


}
