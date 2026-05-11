package com.gadelha.to_do_list.dto.response;

import com.gadelha.to_do_list.model.Task;
import com.gadelha.to_do_list.model.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponseDTO from(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
