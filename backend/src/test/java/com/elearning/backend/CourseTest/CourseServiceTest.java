package com.elearning.backend.CourseTest;

import com.elearning.backend.dto.CourseDTO;
import com.elearning.backend.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CourseServiceTest {

    private final Class<?> serviceClass = CourseService.class;

    @Test
    @DisplayName("CourseService class loads")
    void testServiceClassPresent() {
        assertNotNull(serviceClass);
    }

    @Test
    @DisplayName("saveCourse method signature")
    void testSaveCourseSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod(
                "saveCourse",
                String.class, String.class, String.class, String.class,
                Integer.class, String.class, Long.class,
                MultipartFile.class, MultipartFile.class, MultipartFile.class
        );
        assertEquals(CourseDTO.class, m.getReturnType());
    }

    @Test
    @DisplayName("updateCourse method signature")
    void testUpdateCourseSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod(
                "updateCourse",
                Long.class,
                String.class, String.class, String.class, String.class,
                Integer.class, String.class, Long.class,
                MultipartFile.class, MultipartFile.class, MultipartFile.class
        );
        assertEquals(CourseDTO.class, m.getReturnType());
    }

    @Test
    @DisplayName("getCourseById method signature")
    void testGetCourseByIdSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod("getCourseById", Long.class);
        assertEquals(CourseDTO.class, m.getReturnType());
    }

    @Test
    @DisplayName("getCoursesByInstructorId method signature")
    void testGetCoursesByInstructorIdSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod("getCoursesByInstructorId", Long.class);
        assertEquals(java.util.List.class, m.getReturnType());
    }

    @Test
    @DisplayName("getAllCourses method signature")
    void testGetAllCoursesSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod("getAllCourses", Long.class);
        assertEquals(java.util.List.class, m.getReturnType());
    }

    @Test
    @DisplayName("deleteCourse method signature")
    void testDeleteCourseSignature() throws NoSuchMethodException {
        Method m = serviceClass.getMethod("deleteCourse", Long.class);
        assertEquals(void.class, m.getReturnType());
    }
}
