package com.elearning.backend.mapper;

import com.elearning.backend.dto.NotificationDTO;
import com.elearning.backend.entity.Notification;

public class NotificationMapper {
    public static NotificationDTO toDto(Notification n) {
        NotificationDTO d = new NotificationDTO();
        d.setId(n.getId());
        d.setUserId(n.getUserId());
        d.setType(n.getType());
        d.setMessage(n.getMessage());
        d.setRead(n.getRead());
        d.setCreatedAt(n.getCreatedAt());
        d.setCourseId(n.getCourseId());
        d.setAssessmentId(n.getAssessmentId());
        return d;
    }

    public static Notification toEntity(NotificationDTO d) {
        Notification n = new Notification();
        n.setUserId(d.getUserId());
        n.setType(d.getType());
        n.setMessage(d.getMessage());
        n.setRead(Boolean.TRUE.equals(d.getRead()) ? true : false);
        n.setCourseId(d.getCourseId());
        n.setAssessmentId(d.getAssessmentId());
        return n;
    }
}
