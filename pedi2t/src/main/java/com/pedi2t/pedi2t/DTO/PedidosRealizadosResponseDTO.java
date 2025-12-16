package com.pedi2t.pedi2t.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PedidosRealizadosResponseDTO {
    
    private Long usuarioId;
    private String nombreUsuario;
    private List<PedidoRealizadoDTO> pedidos;
    
    @Data
    public static class PedidoRealizadoDTO {
        private Long idPedido;
        private LocalDate fechaEntrega;
        private String estado;
        private String nombrePlato;
        private String descripcionPlato;
        private String fotoUrl;
        private String diaSemana;
        private Long idMenuDia;
    }
}