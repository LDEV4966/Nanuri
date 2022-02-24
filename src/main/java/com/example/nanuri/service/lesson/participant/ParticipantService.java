package com.example.nanuri.service.lesson.participant;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.lesson.participant.Participant;
import com.example.nanuri.domain.lesson.participant.ParticipantId;
import com.example.nanuri.domain.lesson.participant.ParticipantRepository;
import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
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
public class ParticipantService {

    private final LessonRepository lessonRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    //레슨 참여자 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> findLessonParticipant(Long lessonId){

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // 참여자 조회
        List<Participant> participants = participantRepository.findByLessonId(lessonId);

        List<User> userList =  participants.stream()
                .map( participant ->
                        userRepository.findById(participant.getParticipantId().getUserId()).orElseThrow(()->new UserNotFoundException(ErrorCode.USER_NOT_FOUND))
                )
                .collect(Collectors.toList());

        return userList.stream()
                .map(participant -> new UserResponseDto(participant))
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

        // lesson 찾기
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new LessonNotFoundException(ErrorCode.LESSON_NOT_FOUND));

        // 참여자 조회
        List<Participant> participants = participantRepository.findByLessonId(lessonId);

        return participants.size();
    }


    public Boolean isParticipant(Long lessonId ,Authentication authentication){
        if(authentication==null){
            throw new AuthenticationNullPointerException(ErrorCode.NULL_AUTHENTICATION);
        }
        Long userId = Long.parseLong(authentication.getName());
        Optional<Participant> participant = participantRepository.findById(ParticipantId.builder().userId(userId).lessonId(lessonId).build());
        if(participant.isPresent()){
            return true;
        }
        return false;
    };
}
