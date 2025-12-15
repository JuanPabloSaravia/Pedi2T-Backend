package com.pedi2t.pedi2t.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class HistorialPedidosResponseDTO {
    
    private Long usuarioId;
    private String nombreUsuario;
    private List<HistorialPedidoDTO> pedidos;
    
    @Data
    public static class HistorialPedidoDTO {
        private Long idPedido;
        private String nombrePlato;
        private String categoria;
        private String fotoUrl;
        private LocalDate fechaEntrega;
        private String estado;
    }
}