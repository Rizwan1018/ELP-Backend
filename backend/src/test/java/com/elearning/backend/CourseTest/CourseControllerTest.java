package com.elearning.backend.CourseTest;

import com.elearning.backend.controller.CourseController;
import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private CourseDTO sampleDto(Long id) {
        CourseDTO dto = new CourseDTO();
        dto.setId(id);
        dto.setTitle("Title " + id);
        dto.setDescription("Desc");
        dto.setDomain("Domain");
        dto.setLevel("Beginner");
        dto.setDurationHrs(10);
        dto.setTags("tag1,tag2");
        dto.setInstructorId(99L);
        dto.setThumbnail("thumb.png");
        dto.setVideoUrl("video.mp4");
        dto.setPreRequisite("none");
        dto.setAvgRating(4.5);
        dto.setStudentsCount(100L);
        return dto;
    }

    @Test
    @DisplayName("addCourse calls service and returns DTO")
    void testAddCourse() throws Exception {
        CourseDTO expected = sampleDto(1L);

        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumb.png", "image/png", new byte[]{1});
        MockMultipartFile video = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[]{2});
        MockMultipartFile prerequisite = new MockMultipartFile("prerequisite", "req.pdf", "application/pdf", new byte[]{3});

        when(courseService.saveCourse(
                anyString(), anyString(), anyString(), anyString(),
                any(), anyString(), anyLong(),
                any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class)
        )).thenReturn(expected);

        CourseDTO result = courseController.addCourse(
                "Title", "Description", "Domain", "Level",
                10, "tag1,tag2", 99L,
                thumbnail, video, prerequisite
        );

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        verify(courseService, times(1)).saveCourse(
                eq("Title"), eq("Description"), eq("Domain"), eq("Level"),
                eq(10), eq("tag1,tag2"), eq(99L),
                eq(thumbnail), eq(video), eq(prerequisite)
        );
    }

    @Test
    @DisplayName("updateCourse calls service and returns DTO")
    void testUpdateCourse() throws Exception {
        CourseDTO expected = sampleDto(5L);

        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumb2.png", "image/png", new byte[]{4});
        MockMultipartFile video = new MockMultipartFile("video", "video2.mp4", "video/mp4", new byte[]{5});
        MockMultipartFile prerequisite = new MockMultipartFile("prerequisite", "req2.pdf", "application/pdf", new byte[]{6});

        when(courseService.updateCourse(
                anyLong(),
                anyString(), anyString(), anyString(), anyString(),
                any(), anyString(), anyLong(),
                any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class)
        )).thenReturn(expected);

        CourseDTO result = courseController.updateCourse(
                5L, "Title2", "Description2", "Domain2", "Level2",
                15, "tag3,tag4", 88L,
                thumbnail, video, prerequisite
        );

        assertEquals(expected.getId(), result.getId());
        verify(courseService, times(1)).updateCourse(
                eq(5L), eq("Title2"), eq("Description2"), eq("Domain2"), eq("Level2"),
                eq(15), eq("tag3,tag4"), eq(88L),
                eq(thumbnail), eq(video), eq(prerequisite)
        );
    }

    @Test
    @DisplayName("getCourseById returns DTO")
    void testGetCourseById() {
        CourseDTO expected = sampleDto(3L);
        when(courseService.getCourseById(3L)).thenReturn(expected);

        CourseDTO result = courseController.getCourseById(3L);
        assertEquals(3L, result.getId());
        verify(courseService, times(1)).getCourseById(3L);
    }

    @Test
    @DisplayName("getCourses with instructorId uses getCoursesByInstructorId")
    void testGetCoursesInstructor() {
        List<CourseDTO> list = Arrays.asList(sampleDto(1L), sampleDto(2L));
        when(courseService.getCoursesByInstructorId(99L)).thenReturn(list);

        List<CourseDTO> result = courseController.getCourses(99L, null);
        assertEquals(2, result.size());
        verify(courseService, times(1)).getCoursesByInstructorId(99L);
        verify(courseService, never()).getAllCourses(anyLong());
    }

    @Test
    @DisplayName("getCourses without instructorId uses getAllCourses")
    void testGetCoursesStudent() {
        List<CourseDTO> list = Arrays.asList(sampleDto(7L));
        when(courseService.getAllCourses(55L)).thenReturn(list);

        List<CourseDTO> result = courseController.getCourses(null, 55L);
        assertEquals(1, result.size());
        verify(courseService, times(1)).getAllCourses(55L);
        verify(courseService, never()).getCoursesByInstructorId(anyLong());
    }

    @Test
    @DisplayName("getCourseByInstructorId calls service")
    void testGetCourseByInstructorIdEndpoint() {
        List<CourseDTO> list = Arrays.asList(sampleDto(10L));
        when(courseService.getCoursesByInstructorId(22L)).thenReturn(list);

        List<CourseDTO> result = courseController.getCourseByInstructorId(22L);
        assertEquals(1, result.size());
        verify(courseService, times(1)).getCoursesByInstructorId(22L);
    }

    @Test
    @DisplayName("deleteCourse invokes service delete")
    void testDeleteCourse() {
        doNothing().when(courseService).deleteCourse(11L);

        courseController.deleteCourse(11L);

        verify(courseService, times(1)).deleteCourse(11L);
    }

    @Test
    @DisplayName("Null thumbnail/video/prerequisite still works in addCourse")
    void testAddCourseNullFiles() throws Exception {
        CourseDTO expected = sampleDto(20L);
        when(courseService.saveCourse(
                anyString(), anyString(), anyString(), anyString(),
                any(), anyString(), anyLong(),
                isNull(), isNull(), isNull()
        )).thenReturn(expected);

        CourseDTO result = courseController.addCourse(
                "T", "D", "Dom", "Lvl",
                5, "tags", 123L,
                null, null, null
        );
        assertEquals(20L, result.getId());
        verify(courseService).saveCourse(
                eq("T"), eq("D"), eq("Dom"), eq("Lvl"),
                eq(5), eq("tags"), eq(123L),
                isNull(), isNull(), isNull()
        );
    }
}
