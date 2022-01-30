package com.example.nanuri.controller.lesson.api;

import com.example.nanuri.domain.lesson.Lesson;
import com.example.nanuri.dto.lesson.LessonRequestDto;
import com.example.nanuri.dto.lesson.LessonResponseDto;
import com.example.nanuri.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LessonApiController {

    private final LessonService lessonService;

    @PostMapping(path = "/lesson")
    public void save(@ModelAttribute LessonRequestDto lessonRequestDto) throws IOException {
        lessonService.save(lessonRequestDto);
    }

    @GetMapping(path = "/lesson")
    public List<LessonResponseDto> findAll()  {
        return lessonService.findAll();
    }

    @GetMapping(path = "/lesson/{location}")
    public List<LessonResponseDto> findByLocation(@PathVariable String location)  {
        return lessonService.findByLocation(location);
    }

    @PutMapping(path = "/lesson/{lessonId}/updateStatus")
    public void updateStatus(@PathVariable int lessonId){
        lessonService.updateStatus(lessonId);
    }

    @GetMapping(path = "/lesson/info/{lessonId}")
    public LessonResponseDto findById(@PathVariable int lessonId){
        return lessonService.findById(lessonId);
    }

    @DeleteMapping(path = "/lesson/{lessonId}")
    public void delete(@PathVariable int lessonId){
        lessonService.delete(lessonId);
    }
}
