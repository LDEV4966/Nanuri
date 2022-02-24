package com.example.nanuri.service.lesson.registration;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.participant.Participant;
import com.example.nanuri.domain.lesson.participant.ParticipantId;
import com.example.nanuri.domain.lesson.participant.ParticipantRepository;
import com.example.nanuri.domain.lesson.registration.Registration;
import com.example.nanuri.domain.lesson.registration.RegistrationId;
import com.example.nanuri.domain.lesson.registration.RegistrationRepository;
import com.example.nanuri.domain.user.UserRepository;
import com.example.nanuri.dto.lesson.LessonRegistrationRequestDto;
import com.example.nanuri.dto.lesson.LessonRegistrationResponseDto;
import com.example.nanuri.dto.user.UserResponseDto;
import com.example.nanuri.handler.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegistrationService {

    private final LessonRepository lessonRepository;
    private final RegistrationRepository registrationRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

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
                                .lessonId(registration.getRegistrationId().getLessonId())
                                .user(new UserResponseDto(userRepository.findById(registration.getRegistrationId().getUserId())
                                        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND))))
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

        // 참여자 수 초과이거나, 레슨 등록이 불가능한 상태라면,
        if(lesson.getLimitedNumber() <= participantCount || lesson.getStatus() == false){
            throw new DefaultBadRequestException(ErrorCode.DEFAULT_BAD_REQUEST); // 초과시 에러를 던질 필요가 있을끼? 어차피 프론트에서 막아 놓아서 db에만 등록 안되도록 하자.
        }

        if(lesson.getLimitedNumber()-1 == participantCount){ // 현재 유저 추가 후 참여자 수가 증가시, lesson 의 limitedNumber와 같으면 모집 상태 변경
            lesson.updateStatus();
        }

        //신청 정보 가져오기
        Registration registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build())
                .orElseThrow(() -> new RegistrationNotFoundException(ErrorCode.REGISTRAION_NOT_FOUND));

        //레슨 참여자에 추가하기
        participantRepository.save(
                Participant.builder()
                        .participantId(ParticipantId.builder().lessonId(lessonId).userId(userId).build())
                        .build());

        //신청 내역에서 삭제
        registrationRepository.delete(registration);

        // Todo : 신청자에게 수락 알림 기능

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

        //신청 내역에서 삭제
        registrationRepository.delete(registration);

        // Todo : 신청자에게 거절 알림 기능
    }

    public Boolean isRegistered(Long lessonId , Authentication authentication){
        if(authentication==null){
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        Long userId = Long.parseLong(authentication.getName());
        Optional<Registration> registration = registrationRepository.findById(RegistrationId.builder().userId(userId).lessonId(lessonId).build());
        if(registration.isPresent()){
            return true;
        }
        return false;
    };
}
