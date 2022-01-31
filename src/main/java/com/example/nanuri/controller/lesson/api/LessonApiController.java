package com.example.nanuri.controller.lesson.api;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.dto.http.*;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class LessonApiController {

    private final LessonService lessonService;

    @PostMapping(path = "/lesson")
    public ResponseEntity<? extends BasicResponse> save(@ModelAttribute LessonRequestDto lessonRequestDto) {
        lessonService.save(lessonRequestDto);
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

    @PutMapping(path = "/lesson/{lessonId}/updateStatus")
    public ResponseEntity<? extends BasicResponse> updateStatus(@PathVariable int lessonId){
        lessonService.updateStatus(lessonId);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @GetMapping(path = "/lesson/info/{lessonId}")
    public ResponseEntity<? extends BasicResponse> findById(@PathVariable int lessonId){
        LessonResponseDto lessonResponseDto = lessonService.findById(lessonId);
        return ResponseEntity.ok().body(new SuccessDataResponse<LessonResponseDto>(lessonResponseDto));
    }

    @DeleteMapping(path = "/lesson/{lessonId}")
    public ResponseEntity<? extends BasicResponse> delete(@PathVariable int lessonId){
        if(!lessonService.delete(lessonId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(MessageEnum.NOT_FOUND, HttpStatusEnum.NOT_FOUND));
        }
        return ResponseEntity.ok().body(new SuccessResponse());
    }


}
