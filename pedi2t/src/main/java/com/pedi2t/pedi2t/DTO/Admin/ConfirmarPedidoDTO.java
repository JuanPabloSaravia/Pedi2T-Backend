package com.pedi2t.pedi2t.DTO.Admin;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmarPedidoDTO {
    
    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;
    
    @NotNull(message = "La fecha fin no puede ser nula")  
    private LocalDate fechaFin;
}