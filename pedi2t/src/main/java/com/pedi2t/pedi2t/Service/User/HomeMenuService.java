package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.HomeMenusResponseDTO;

public interface HomeMenuService {
    HomeMenusResponseDTO obtenerMenusUsuario(Long usuarioId);
}