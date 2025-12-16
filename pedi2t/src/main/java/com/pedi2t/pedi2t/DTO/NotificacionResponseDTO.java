package com.pedi2t.pedi2t.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Long id;
    private String titulo; // mapea desde asunto
    private String mensaje;
    private LocalDateTime fecha; // mapea desde fechaEnvio
    private boolean leida;
    private String tipo;
}
