package com.pedi2t.pedi2t.DTO.Admin;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EntregarPedidosResponseDTO {
    
    private Integer cantidadPedidosEntregados;
    private String mensaje;
    private LocalDate fechaEntrega;
}