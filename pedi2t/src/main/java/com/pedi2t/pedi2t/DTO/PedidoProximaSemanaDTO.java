package com.pedi2t.pedi2t.DTO;

import lombok.Data;

@Data
public class PedidoProximaSemanaDTO {
    
    private Long menuPlatoId;
    private String diaSemana;
    private PlatoDTO plato;
    private String menuDiaDescripcion;
}
