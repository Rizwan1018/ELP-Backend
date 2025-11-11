package com.elearning.backend.service;

import com.elearning.backend.dto.NotificationDTO;
import com.elearning.backend.mapper.NotificationMapper;
import com.elearning.backend.entity.Notification;
import com.elearning.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public List<NotificationDTO> listForUser(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(NotificationMapper::toDto).toList();
    }

    public Long unreadCount(Long userId) {
        return repo.countByUserIdAndReadFalse(userId);
    }

    public NotificationDTO create(NotificationDTO dto) {
        Notification saved = repo.save(NotificationMapper.toEntity(dto));
        return NotificationMapper.toDto(saved);
    }

    public NotificationDTO markRead(Long id) {
        Notification n = repo.findById(id).orElseThrow();
        n.setRead(true);
        return NotificationMapper.toDto(repo.save(n));
    }
}
