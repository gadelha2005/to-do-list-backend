package com.gadelha.to_do_list.service;

import com.gadelha.to_do_list.dto.request.TaskRequestDTO;
import com.gadelha.to_do_list.dto.response.TaskResponseDTO;
import com.gadelha.to_do_list.exception.TaskNotFoundException;
import com.gadelha.to_do_list.model.Task;
import com.gadelha.to_do_list.model.User;
import com.gadelha.to_do_list.model.enums.TaskStatus;
import com.gadelha.to_do_list.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User mockUser() {
        return User.builder().id(1L).name("Pedro").email("pedro@email.com").password("hash").build();
    }

    private Task mockTask() {
        return Task.builder()
                .id(1L)
                .title("Estudar JWT")
                .description("Focar em filtros")
                .status(TaskStatus.PENDING)
                .user(mockUser())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve criar tarefa com status PENDING quando status não informado")
    void create_withoutStatus_defaultsPending() {
        TaskRequestDTO dto = new TaskRequestDTO("Estudar JWT", null, null);
        Task saved = mockTask();

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponseDTO result = taskService.create(dto, mockUser());

        assertNotNull(result);
        assertEquals("Estudar JWT", result.title());
        assertEquals(TaskStatus.PENDING, result.status());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Deve criar tarefa com status informado no DTO")
    void create_withStatus_usesProvidedStatus() {
        TaskRequestDTO dto = new TaskRequestDTO("Estudar JWT", null, TaskStatus.IN_PROGRESS);
        Task saved = Task.builder()
                .id(1L).title("Estudar JWT").status(TaskStatus.IN_PROGRESS)
                .user(mockUser()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponseDTO result = taskService.create(dto, mockUser());

        assertEquals(TaskStatus.IN_PROGRESS, result.status());
    }

    @Test
    @DisplayName("Deve retornar todas as tarefas do usuário sem filtro")
    void listAll_withoutFilter_returnsAllUserTasks() {
        when(taskRepository.findByUserId(1L)).thenReturn(List.of(mockTask()));

        List<TaskResponseDTO> result = taskService.listAll(1L, null);

        assertEquals(1, result.size());
        verify(taskRepository).findByUserId(1L);
        verify(taskRepository, never()).findByUserIdAndStatus(any(), any());
    }

    @Test
    @DisplayName("Deve filtrar tarefas por status")
    void listAll_withStatusFilter_returnsFilteredTasks() {
        when(taskRepository.findByUserIdAndStatus(1L, TaskStatus.PENDING)).thenReturn(List.of(mockTask()));

        List<TaskResponseDTO> result = taskService.listAll(1L, TaskStatus.PENDING);

        assertEquals(1, result.size());
        verify(taskRepository).findByUserIdAndStatus(1L, TaskStatus.PENDING);
        verify(taskRepository, never()).findByUserId(any());
    }

    @Test
    @DisplayName("Deve retornar tarefa quando ID e usuário são válidos")
    void getById_success_returnsDTO() {
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(mockTask()));

        TaskResponseDTO result = taskService.getById(1L, 1L);

        assertNotNull(result);
        assertEquals("Estudar JWT", result.title());
    }

    @Test
    @DisplayName("Deve lançar TaskNotFoundException quando tarefa não existe")
    void getById_notFound_throwsException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(99L, 1L));
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos presentes no DTO")
    void update_partialFields_updatesOnlyProvided() {
        Task task = mockTask();
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskRequestDTO dto = new TaskRequestDTO(null, null, TaskStatus.DONE);
        TaskResponseDTO result = taskService.update(1L, dto, 1L);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals("Estudar JWT", task.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Deve lançar TaskNotFoundException ao atualizar tarefa inexistente")
    void update_notFound_throwsException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.update(99L, new TaskRequestDTO("x", null, null), 1L));
    }

    @Test
    @DisplayName("Deve deletar tarefa quando encontrada")
    void delete_success_callsRepository() {
        Task task = mockTask();
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(task));

        taskService.delete(1L, 1L);

        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("Deve lançar TaskNotFoundException ao deletar tarefa inexistente")
    void delete_notFound_throwsException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.delete(99L, 1L));
    }
}
