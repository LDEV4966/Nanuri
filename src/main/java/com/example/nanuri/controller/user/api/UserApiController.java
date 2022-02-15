package com.example.nanuri.controller.user.api;

import com.example.nanuri.dto.http.BasicResponse;
import com.example.nanuri.dto.http.SuccessDataResponse;
import com.example.nanuri.dto.user.UserLessonResponseDto;
import com.example.nanuri.dto.user.UserResponseDto;
import com.example.nanuri.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping(path = "/user/info/{userId}")
    public ResponseEntity<? extends BasicResponse> findById(@PathVariable Long userId){
        UserResponseDto userResponseDto = userService.findById(userId);
        return ResponseEntity.ok().body(new SuccessDataResponse<UserResponseDto>(userResponseDto));
    }

    @GetMapping(path = "/user/{userId}/lesson")
    public ResponseEntity<? extends BasicResponse> findUserLessonById(@PathVariable Long userId){
        UserLessonResponseDto userLessonResponseDto = userService.findUserLessonById(userId);
        return ResponseEntity.ok().body(new SuccessDataResponse<UserLessonResponseDto>(userLessonResponseDto));
    }

    @GetMapping(path = "/user/{userId}/lesson/subscription")
    public ResponseEntity<? extends BasicResponse> findUserSubscribedLessonById(@PathVariable Long userId){
        UserLessonResponseDto userLessonResponseDto = userService.findUserSubscribedLessonById(userId);
        return ResponseEntity.ok().body(new SuccessDataResponse<UserLessonResponseDto>(userLessonResponseDto));
    }

}
