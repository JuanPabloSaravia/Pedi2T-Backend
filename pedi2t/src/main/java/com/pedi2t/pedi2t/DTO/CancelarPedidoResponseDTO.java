package com.pedi2t.pedi2t.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CancelarPedidoResponseDTO {
    
    private Long idPedido;
    private String estado;
    private String mensaje;
    private LocalDate fechaEntrega;
    private String nombrePlato;
}