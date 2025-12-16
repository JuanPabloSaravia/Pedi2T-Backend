package com.pedi2t.pedi2t.DTO.Admin;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ConfirmarPedidoResponseDTO {
    
    private Integer cantidadPedidosConfirmados;
    private String mensaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String rutaArchivoResumen;
}