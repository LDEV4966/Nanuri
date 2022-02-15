package com.example.nanuri.controller.lesson.api;

import com.example.nanuri.dto.http.*;
import com.example.nanuri.dto.lesson.*;
import com.example.nanuri.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class LessonApiController {

    private final LessonService lessonService;

    @PostMapping(path = "/lesson")
    public ResponseEntity<? extends BasicResponse> save(@ModelAttribute LessonRequestDto lessonRequestDto,Authentication authentication) {
        lessonService.save(lessonRequestDto,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @GetMapping(path = "/lesson")
    public ResponseEntity<? extends BasicResponse> findAll()  {
        List<LessonResponseDto> lessonResponseDtos = lessonService.findAll();
        return ResponseEntity.ok().body(new SuccessDataResponse<List<LessonResponseDto>>(lessonResponseDtos));
    }

    @GetMapping(path = "/lesson/{location}")
    public ResponseEntity<? extends BasicResponse> findByLocation(@PathVariable String location)  {
        List<LessonResponseDto> lessonResponseDtos = lessonService.findByLocation(location);
        return ResponseEntity.ok().body(new SuccessDataResponse<List<LessonResponseDto>>(lessonResponseDtos));
    }

    @DeleteMapping(path = "/lesson/{lessonId}")
    public ResponseEntity<? extends BasicResponse> delete(@PathVariable Long lessonId , Authentication authentication){
        lessonService.delete(lessonId , authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @PutMapping(path = "/lesson/{lessonId}/updateStatus")
    public ResponseEntity<? extends BasicResponse> updateStatus(@PathVariable Long lessonId , Authentication authentication){
        lessonService.updateStatus(lessonId,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @GetMapping(path = "/lesson/info/{lessonId}")
    public ResponseEntity<? extends BasicResponse> findById(@PathVariable Long lessonId){
        LessonResponseDto lessonResponseDto = lessonService.findById(lessonId);
        return ResponseEntity.ok().body(new SuccessDataResponse<LessonResponseDto>(lessonResponseDto));
    }

    @GetMapping(path = "/lesson/{lessonId}/registration")
    public ResponseEntity<? extends BasicResponse>  findLessonRegistrationInfoByLessonId(@PathVariable Long lessonId, Authentication authentication){
         List<LessonRegistrationResponseDto> registrationResponseDtos = lessonService.findLessonRegistrationInfoByLessonId(lessonId,authentication);
        return ResponseEntity.ok().body(new SuccessDataResponse<List<LessonRegistrationResponseDto>>(registrationResponseDtos));
    }

    @PostMapping(path = "/lesson/{lessonId}/registration")
    public ResponseEntity<? extends BasicResponse> saveRegistrationInfo(@PathVariable Long lessonId , Authentication authentication, @RequestBody LessonRegistrationRequestDto lessonRegistrationRequestDto){
        lessonService.saveRegistrationInfo(lessonId,authentication,lessonRegistrationRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    // 상태 수정 겸 PARTICIPANT 에 삽입하는거라 POST 인지 PUT인지 헷갈리는데 Form 이나 뭘 받는게 아니니, PUT 으로 두겠음.
    @PutMapping(path = "/lesson/{lessonId}/registration/accept/{userId}")
    public ResponseEntity<? extends BasicResponse> acceptLessonRegistration(@PathVariable Long lessonId,@PathVariable Long userId , Authentication authentication){
        lessonService.acceptLessonRegistration(lessonId,userId,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @PutMapping(path = "/lesson/{lessonId}/registration/deny/{userId}")
    public ResponseEntity<? extends BasicResponse> denyLessonRegistration(@PathVariable Long lessonId,@PathVariable Long userId , Authentication authentication){
        lessonService.denyLessonRegistration(lessonId,userId,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @DeleteMapping(path = "/lesson/{lessonId}/registration/{userId}")
    public ResponseEntity<? extends BasicResponse> deleteLessonRegistration(@PathVariable Long lessonId,@PathVariable Long userId , Authentication authentication){
        lessonService.deleteLessonRegistration(lessonId,userId,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @GetMapping(path = "/lesson/{lessonId}/participant")
    public ResponseEntity<? extends BasicResponse> findLessonParticipant(@PathVariable Long lessonId){
        List<LessonParticipantResponseDto> lessonParticipantResponseDtos = lessonService.findLessonParticipant(lessonId);
        return ResponseEntity.ok().body(new SuccessDataResponse<List<LessonParticipantResponseDto>>(lessonParticipantResponseDtos));
    }

    @GetMapping(path = "/lesson/{lessonId}/participant/count")
    public ResponseEntity<? extends BasicResponse> findLessonParticipantCount(@PathVariable Long lessonId){
        int participantCount = lessonService.findLessonParticipantCount(lessonId);
        return ResponseEntity.ok().body(new SuccessDataResponse<Integer>(participantCount));
    }

    @DeleteMapping(path = "/lesson/{lessonId}/participant/{userId}")
    public ResponseEntity<? extends BasicResponse> deleteLessonParticipant(@PathVariable Long lessonId,@PathVariable Long userId, Authentication authentication){
        lessonService.deleteLessonParticipant(lessonId,userId,authentication);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

}
