package com.gadelha.to_do_list.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        List<FieldErrorDTO> errors,
        LocalDateTime timestamp
) {
    public record FieldErrorDTO(String field, String message) {}
}
