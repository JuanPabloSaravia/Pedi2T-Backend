package com.pedi2t.pedi2t.DTO.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificarRequestDTO {
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 1, max = 100, message = "El asunto debe tener entre 1 y 100 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 1, max = 500, message = "El mensaje debe tener entre 1 y 500 caracteres")
    private String mensaje;
}
