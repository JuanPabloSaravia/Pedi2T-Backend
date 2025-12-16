package com.pedi2t.pedi2t.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelarPedidoDTO {
    
    @NotNull(message = "El ID del pedido no puede ser nulo")
    private Long idPedido;
    
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario;
}