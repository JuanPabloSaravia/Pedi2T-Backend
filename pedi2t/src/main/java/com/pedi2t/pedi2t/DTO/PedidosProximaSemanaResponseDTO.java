package com.pedi2t.pedi2t.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PedidosProximaSemanaResponseDTO {
    
    private Long usuarioId;
    private String nombreUsuario;
    private List<PedidoProximaSemanaDTO> pedidos;
}
