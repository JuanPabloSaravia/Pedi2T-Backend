package com.pedi2t.pedi2t.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDiaDTO {
    private Long id;
    private String fecha; // ISO string
    private String descripcion;
    private List<PlatoDTO> platos;
}
