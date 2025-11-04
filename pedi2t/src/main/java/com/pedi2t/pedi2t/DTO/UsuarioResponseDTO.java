package com.pedi2t.pedi2t.DTO;


import java.util.ArrayList;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private ArrayList<String> diasPresenciales;
}
