package com.example.nanuri.controller.lesson.api;

import com.example.nanuri.dto.http.*;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
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



}
