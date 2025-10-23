package com.elearning.backend.controller;


import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.model.Instructor;
import com.elearning.backend.service.CourseService;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {


    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDTO> getCourses(@RequestParam(required = false) Long instructorId) {
        if (instructorId != null) {
            return courseService.getCoursesByInstructorId(instructorId);
        }
        return courseService.getAllCourses();
    }

    @GetMapping("/instructor/{instructorId}")
    public List<CourseDTO> getCourseByInstructorId(@PathVariable Long instructorId){
        return courseService.getCoursesByInstructorId(instructorId);
    }

    @PostMapping
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.saveCourse(courseDTO);
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public CourseDTO updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);  // Set ID so it updates instead of creating new
        return courseService.saveCourse(courseDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}

