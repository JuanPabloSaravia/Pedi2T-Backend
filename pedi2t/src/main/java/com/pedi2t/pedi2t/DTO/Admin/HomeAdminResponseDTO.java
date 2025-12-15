package com.pedi2t.pedi2t.DTO.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeAdminResponseDTO {
    private Long adminId;
    private List<MenuDiaAdminDTO> menus;
}