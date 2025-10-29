package com.elearning.backend.controller;


import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.model.Instructor;
import com.elearning.backend.service.CourseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.awt.*;
import java.util.List;


@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {


    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CourseDTO addCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("domain") String domain,
            @RequestParam("level") String level,
            @RequestParam("durationHrs") Integer durationHrs,
            @RequestParam("tags") String tags,
            @RequestParam("instructorId") Long instructorId,
            @RequestParam(value = "thumbnail", required = false)MultipartFile thumbnail,
            @RequestParam(value = "video", required = false)MultipartFile video,
            @RequestParam(value = "prerequisite", required = false)MultipartFile prerequisite ) throws Exception {
        return courseService.saveCourse(title, description, domain, level,durationHrs, tags, instructorId, thumbnail, video, prerequisite);
    }



    @GetMapping
    public List<CourseDTO> getCourses(
            @RequestParam(required = false) Long instructorId,
            @RequestParam(required = false) Long studentId) {
        if (instructorId != null) {
            return courseService.getCoursesByInstructorId(instructorId);
        }
        return courseService.getAllCourses(studentId);
    }

    @GetMapping("/instructor/{instructorId}")
    public List<CourseDTO> getCourseByInstructorId(@PathVariable Long instructorId){
        return courseService.getCoursesByInstructorId(instructorId);
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CourseDTO updateCourse(@PathVariable Long id,
                                  @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("domain") String domain,
                                  @RequestParam("level") String level,
                                  @RequestParam("durationHrs") Integer durationHrs,
                                  @RequestParam("tags") String tags,
                                  @RequestParam("instructorId") Long instructorId,
                                  @RequestParam(value = "thumbnail", required = false)MultipartFile thumbnail,
                                  @RequestParam(value = "video", required = false)MultipartFile video,
                                  @RequestParam(value = "prerequisite", required = false)MultipartFile prerequisite ) throws Exception {
        return courseService.updateCourse(id,title, description, domain, level,durationHrs, tags, instructorId, thumbnail, video, prerequisite);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}

