package com.example.nanuri.service.lesson;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.lessonImg.LessonImg;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgId;
import com.example.nanuri.domain.lesson.lessonImg.LessonImgRepository;
import com.example.nanuri.domain.lesson.participant.Participant;
import com.example.nanuri.domain.lesson.participant.ParticipantId;
import com.example.nanuri.domain.lesson.participant.ParticipantRepository;
import com.example.nanuri.domain.lesson.registration.Registration;
import com.example.nanuri.domain.lesson.registration.RegistrationId;
import com.example.nanuri.domain.lesson.registration.RegistrationRepository;
import com.example.nanuri.domain.lesson.registration.RegistrationStatus;
import com.example.nanuri.dto.lesson.*;
import com.example.nanuri.handler.exception.*;
import com.example.nanuri.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonImgRepository lessonImgRepository;
    private final RegistrationRepository registrationRepository;
    private final ParticipantRepository participantRepository;
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

        // Todo : 참여자 및 신청서 삭제

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
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
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

        // participant에 본인이 있다면 신청 불가능
        Optional<Participant> participant = participantRepository.findById(ParticipantId.builder().lessonId(lessonId).userId(userId).build());
        if (participant.isPresent()){
            throw new DuplicatedRegistrationException(ErrorCode.DUPLICATE_REGISTRATION);
        }

        // Todo : 본인이 해당 레슨의 생성자일 경우 신청 불가능 => Test를 위해서 임시로 주석 처리 해둠.
//        if (lesson.getCreator() == Long.parseLong(authentication.getName())){
//            throw new DefaultBadRequestException(ErrorCode.DEFAULT_BAD_REQUEST);
//        }

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
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }

        // lesson의 현재 수강인원과 수강정원 비교 후 신청 후 정원 초과라면 상태 업데이트
        int participantCount = participantRepository.findByLessonId(lessonId).size();
        if(lesson.getLimitedNumber()-1 == participantCount){
            lesson.updateStatus();
        }
        if(lesson.getLimitedNumber() <= participantCount){
            return; // 초과시 에러를 던질 필요가 있을끼? 어차피 프론트에서 막아 놓아서 db에만 등록 안되도록 하자.
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        // Status Enum 수정
        registration.updateRegistraionStatus(RegistrationStatus.ACCEPTED);

        //레슨 참여자에 추가하기
        participantRepository.save(
                Participant.builder()
                        .participantId(ParticipantId.builder().lessonId(lessonId).userId(userId).build())
                        .build());

    }

    //레슨 신청거절
    @Transactional
    public void denyLessonRegistration(Long lessonId,Long userId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson 생성자만 레슨 신청거절 가능
        if(lesson.getCreator() != Long.parseLong(authentication.getName())){
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        // Status Enum 수정
        registration.updateRegistraionStatus(RegistrationStatus.DENIED);

        // Todo : 신청자에게 거절 알림 기능
    }

    //레슨 신청 삭제
    @Transactional
    public void deleteLessonRegistration(Long lessonId,Long userId, Authentication authentication){
        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // lesson 생성자만 레슨 신청 삭제 가능
        if(lesson.getCreator() != Long.parseLong(authentication.getName())){
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        //신청 기록 삭제
        registrationRepository.delete(registration);
    }

    //레슨 참여자 조회
    @Transactional(readOnly = true)
    public List<LessonParticipantResponseDto> findLessonParticipant(Long lessonId){

        // 참여자 조회
        List<Participant> participants = participantRepository.findByLessonId(lessonId);

        return participants.stream()
                .map( participant ->
                        LessonParticipantResponseDto
                                .builder()
                                .userId(participant.getParticipantId().getUserId())
                                .lessonId(participant.getParticipantId().getLessonId())
                                .build())
                .collect(Collectors.toList());
    }

    // 레슨 참여자 삭제 (탈퇴 기능)
    @Transactional
    public void deleteLessonParticipant(Long lessonId,Long userId, Authentication authentication){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        //참여자 정보 찾기
        Participant participant = participantRepository.findById(ParticipantId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new ParticipantNotFoundException(ErrorCode.PARTICIPANT_NOT_FOUND));

        // 본인이거나 생성자만이 삭제가 가능해야 한다.
        if (Long.parseLong(authentication.getName()) == lesson.getCreator() || Long.parseLong(authentication.getName()) == userId ){
            participantRepository.delete(participant);
        } else {
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }

    }

    //레슨 참여자 수 조회
    @Transactional(readOnly = true)
    public int findLessonParticipantCount(Long lessonId){
        // 참여자 조회
        List<Participant> participants = participantRepository.findByLessonId(lessonId);

        return participants.size();
    }


    //생성자와 현 유저가 동일한 지 확인
    private boolean isAuthorizedUser(Long creatorId ,Authentication authentication){
        if(authentication==null){
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        if( Long.parseLong(authentication.getName()) != creatorId){
            throw new UnAuthorizedUserException(ErrorCode.UNAUTHORIZED_USER);
        }
        return true;
    }

}
