package com.elearning.backend.service;

import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.model.Course;
import com.elearning.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    private CourseDTO convertToDTO(Course course){
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setInstructorId(course.getInstructorId());
        dto.setDomain(course.getDomain());
        dto.setLevel(course.getLevel());
        dto.setDurationHrs(course.getDurationHrs());
        dto.setTags(course.getTags() != null ? String.join(",", course.getTags()):null);
        dto.setDescription(course.getDescription());
        dto.setThumbnail(course.getThumbnail());
        dto.setVideoUrl(course.getVideoUrl());
        return dto;
    }

    private Course convertToEntity(CourseDTO dto){
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setInstructorId(dto.getInstructorId());
        course.setDomain(dto.getDomain());
        course.setLevel(dto.getLevel());
        course.setDurationHrs(dto.getDurationHrs());
        course.setTags(dto.getTags() != null ? List.of(dto.getTags().split(",")): null);
        course.setDescription(dto.getDescription());
        course.setThumbnail(dto.getThumbnail());
        course.setVideoUrl(dto.getVideoUrl());
        return course;
    }


    public List<CourseDTO> getAllCourses(){
        return courseRepository.findAll()
        .stream().map(this::convertToDTO)
                .collect(Collectors.toList()) ;
    }


    public List<CourseDTO> getCoursesByInstructorId(Long instructorId){
        return courseRepository.findByInstructorId(instructorId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CourseDTO saveCourse(CourseDTO dto){
        Course course = convertToEntity(dto);
        Course saved = courseRepository.save(course);
        return convertToDTO(saved);
    }

    public CourseDTO getCourseById(Long id){
        return courseRepository.findById(id).map(this::convertToDTO)
                .orElse(null);
    }

    public void deleteCourse(Long id){
        courseRepository.deleteById(id);
    }
}
