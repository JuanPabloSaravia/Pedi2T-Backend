package com.pedi2t.pedi2t.DTO.Admin;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntregarPedidosDTO {
    
    @NotNull(message = "La fecha de entrega no puede ser nula")
    private LocalDate fechaEntrega;
}