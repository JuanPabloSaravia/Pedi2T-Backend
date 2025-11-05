package com.pedi2t.pedi2t.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponseDTO {
    private long usuariosCount;
    private long platosCount;
    private long diasPresencialesCount;
    private String lastUsuarioCreacion; // ISO string o null
    private List<PlatoDTO> platos;

}
