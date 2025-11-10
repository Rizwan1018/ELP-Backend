package com.elearning.backend.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String type;
    private String message;
    private Boolean read;
    private Instant createdAt;
    private Long courseId;
    private Long assessmentId;
}
