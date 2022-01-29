package com.example.nanuri.controller.lesson.api;

import com.example.nanuri.dto.LessonRequestDto;
import com.example.nanuri.dto.LessonSaveRequestDto;
import com.example.nanuri.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LessonApiController {

    private final LessonService lessonService;

    @PostMapping(path = "/lesson")
    public void save(@ModelAttribute LessonRequestDto lessonRequestDto) throws IOException {
        lessonService.save(lessonRequestDto);
    }
}
