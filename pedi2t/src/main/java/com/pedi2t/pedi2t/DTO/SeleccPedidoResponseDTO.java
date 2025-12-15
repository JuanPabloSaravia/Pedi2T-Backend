package com.pedi2t.pedi2t.DTO;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeleccPedidoResponseDTO {

    Long idPedido;
    LocalDate fechaEntrega;
    
}
