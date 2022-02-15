package com.example.nanuri.service.user;

import com.example.nanuri.domain.lesson.LessonRepository;
import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.dto.user.UserLessonResponseDto;
import com.example.nanuri.dto.user.UserResponseDto;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.UserNotFoundException;
import com.example.nanuri.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LessonService lessonService;

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserLessonResponseDto findUserLessonById(Long userId) {
        UserResponseDto userResponseDto = findById(userId);
        List<LessonResponseDto> lessonResponseDtos = lessonService.findByCreator(userId);
        return new UserLessonResponseDto(userResponseDto,lessonResponseDtos);
    }

    @Transactional(readOnly = true)
    public UserLessonResponseDto findUserSubscribedLessonById(Long userId){
        UserResponseDto userResponseDto = findById(userId);
        List<LessonResponseDto> lessonResponseDtos = lessonService.findSubscribedLesson(userId);
        return new UserLessonResponseDto(userResponseDto,lessonResponseDtos);
    }

}
