package com.elearning.backend.service;

import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.model.Course;
import com.elearning.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
        course.setPreRequisite(dto.getPreRequisite());
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


    public CourseDTO saveCourse(String title, String description, String domain, String level,Integer durationHrs, String tags, Long instructorId, MultipartFile thumbnail, MultipartFile video, MultipartFile prerequisite) throws Exception {

        try{
            String thumbnailPath = saveFile(thumbnail, "thumbnails");
            String videoPath = saveFile(video, "videos");
            String prerequisitePath = saveFile(prerequisite, "prerequisites");

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setDomain(domain);
            course.setLevel(level);
            course.setDurationHrs(durationHrs);
            course.setTags(Arrays.asList(tags.split(",")));
            course.setInstructorId(instructorId);
            course.setThumbnail(thumbnailPath);
            course.setVideoUrl(videoPath);
            course.setPreRequisite(prerequisitePath);

            Course saved = courseRepository.save(course);
            return convertToDTO(saved);
        } catch(Exception e){
            throw new Exception("Something went wrong",e);
        }
    }

    private static final String basePath = "C:/Users/2441337/angular/ELP/uploads";

    private String saveFile(MultipartFile file, String subFolder) throws IOException {
        if(file == null || file.isEmpty()) return null;

        File baseDir = new File(basePath);
        if(!baseDir.exists()) baseDir.mkdirs();

        File folder = new File(baseDir, subFolder);
        if(!folder.exists()) folder.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File destination = new File(folder, fileName);
        file.transferTo(destination);

        return "uploads/" +subFolder + "/" +fileName;

    }


}
