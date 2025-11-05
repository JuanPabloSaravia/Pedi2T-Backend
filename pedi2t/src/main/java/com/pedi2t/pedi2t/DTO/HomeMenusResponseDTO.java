package com.pedi2t.pedi2t.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeMenusResponseDTO {
    private Long usuarioId;
    private List<MenuDiaDTO> menus;
}
