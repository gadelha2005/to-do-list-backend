package com.gadelha.to_do_list.service;

import com.gadelha.to_do_list.dto.request.TaskRequestDTO;
import com.gadelha.to_do_list.dto.response.TaskResponseDTO;
import com.gadelha.to_do_list.exception.TaskNotFoundException;
import com.gadelha.to_do_list.model.Task;
import com.gadelha.to_do_list.model.User;
import com.gadelha.to_do_list.model.enums.Priority;
import com.gadelha.to_do_list.model.enums.TaskStatus;
import com.gadelha.to_do_list.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponseDTO create(TaskRequestDTO dto, User user) {
        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status() != null ? dto.status() : TaskStatus.PENDING)
                .priority(dto.priority() != null ? dto.priority() : Priority.MEDIUM)
                .dueDate(dto.dueDate())
                .user(user)
                .build();

        return TaskResponseDTO.from(taskRepository.save(task));
    }

    public List<TaskResponseDTO> listAll(Long userId, TaskStatus status) {
        List<Task> tasks = status != null
                ? taskRepository.findByUserIdAndStatus(userId, status)
                : taskRepository.findByUserId(userId);

        return tasks.stream().map(TaskResponseDTO::from).toList();
    }

    public TaskResponseDTO getById(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return TaskResponseDTO.from(task);
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO dto, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (dto.title() != null)       task.setTitle(dto.title());
        if (dto.description() != null) task.setDescription(dto.description());
        if (dto.status() != null)      task.setStatus(dto.status());
        if (dto.priority() != null)    task.setPriority(dto.priority());
        if (dto.dueDate() != null)     task.setDueDate(dto.dueDate());

        return TaskResponseDTO.from(taskRepository.save(task));
    }

    public void delete(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
}
