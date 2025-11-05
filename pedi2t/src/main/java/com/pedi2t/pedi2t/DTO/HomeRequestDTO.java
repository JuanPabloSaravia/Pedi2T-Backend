package com.pedi2t.pedi2t.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HomeRequestDTO {
    @NotNull(message = "El ID de usuario es requerido")
    private Long idUsuario;
}