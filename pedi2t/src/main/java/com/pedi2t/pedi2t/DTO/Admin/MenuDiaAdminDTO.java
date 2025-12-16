package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDiaAdminDTO {
    private Long id;
    private String descripcion;
    private String diaSemana;
    private List<PlatoAdminDTO> platos;
}