package com.elearning.backend.controller;

import com.elearning.backend.dto.AnnouncementDTO;
import com.elearning.backend.model.Announcement;
import com.elearning.backend.service.AnnouncementService;
import com.elearning.backend.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnouncementController {
    private final AnnouncementService service;
    private final CourseService courseService;

    public AnnouncementController(AnnouncementService s, CourseService cs) {
        this.service = s; this.courseService = cs;
    }

    @GetMapping(params = "courseId")
    public List<Announcement> byCourse(@RequestParam Long courseId) {
        return service.byCourse(courseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Announcement create(@RequestBody AnnouncementDTO dto) {
        // fetch course title only for a nicer notification message
        var courseDto = courseService.getCourseById(dto.courseId);
        String title = courseDto != null ? courseDto.getTitle() : "Course";
        return service.create(dto, title);
    }
}
