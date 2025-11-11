package com.elearning.backend.mapper;

import com.elearning.backend.dto.AnnouncementDTO;
import com.elearning.backend.entity.Announcement;

public class AnnouncementMapper {
    public static AnnouncementDTO toDto(Announcement a) {
        AnnouncementDTO d = new AnnouncementDTO();
        d.setId(a.getId());
        d.setCourseId(a.getCourseId());
        d.setInstructorId(a.getInstructorId());
        d.setTitle(a.getTitle());
        d.setMessage(a.getMessage());
        d.setCreatedAt(a.getCreatedAt());
        return d;
    }
    public static Announcement toEntity(AnnouncementDTO d) {
        Announcement a = new Announcement();
        a.setCourseId(d.getCourseId());
        a.setInstructorId(d.getInstructorId());
        a.setTitle(d.getTitle());
        a.setMessage(d.getMessage());
        return a;
    }
}
