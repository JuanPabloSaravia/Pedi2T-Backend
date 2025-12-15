package com.pedi2t.pedi2t.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeleccionarPedidoDTO {

    @NotNull(message = "El idUsuario no puede ser nulo")
    private Long idUsuario;

    @NotNull(message = "El idPlato no puede ser nulo")
    private Long idPlato;

    @NotNull(message = "El idMenuDia no puede ser nulo")
    private Long idMenuDia;

    @NotBlank(message = "El diaEntrega no puede estar vacío")
    private String diaEntrega;

}
