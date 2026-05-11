package com.gadelha.to_do_list.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Tarefa com id " + id + " não encontrada");
    }
}
