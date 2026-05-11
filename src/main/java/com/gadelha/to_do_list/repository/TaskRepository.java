package com.gadelha.to_do_list.repository;

import com.gadelha.to_do_list.model.Task;
import com.gadelha.to_do_list.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
