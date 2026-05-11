package com.gadelha.to_do_list.controller;

import com.gadelha.to_do_list.dto.request.TaskRequestDTO;
import com.gadelha.to_do_list.dto.response.TaskResponseDTO;
import com.gadelha.to_do_list.model.User;
import com.gadelha.to_do_list.model.enums.TaskStatus;
import com.gadelha.to_do_list.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tarefas")
@SecurityRequirement(name = "bearer-key")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Criar tarefa")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO dto,
                                                  @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(dto, user));
    }

    @GetMapping
    @Operation(summary = "Listar tarefas (filtro opcional por status)")
    public ResponseEntity<List<TaskResponseDTO>> listAll(@RequestParam(required = false) TaskStatus status,
                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.listAll(user.getId(), status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getById(id, user.getId()));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody TaskRequestDTO dto,
                                                  @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.update(id, dto, user.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover tarefa")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        taskService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
