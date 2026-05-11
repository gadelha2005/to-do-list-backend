package com.gadelha.to_do_list.dto.request;

import com.gadelha.to_do_list.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255)
        String title,

        String description,

        TaskStatus status
) {}
