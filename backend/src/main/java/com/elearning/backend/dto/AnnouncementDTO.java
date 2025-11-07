package com.elearning.backend.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class AnnouncementDTO {
    public Long id;
    public Long courseId;
    public Long instructorId;
    public String title;
    public String message;
    public Instant createdAt;
}
