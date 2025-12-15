package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;
import java.util.List;

@Data
public class PlatosPedidosResponseDTO {
    private List<PlatoPedidoDTO> platos;
    private Integer totalPedidos;
    private String mensaje;
    
    @Data
    public static class PlatoPedidoDTO {
        private Long idPlato;
        private String nombrePlato;
        private String categoria;
        private String imagenUrl;
        private Integer cantidadPendiente;
        private Integer cantidadConfirmado;
        private Integer cantidadTotal;
    }
}