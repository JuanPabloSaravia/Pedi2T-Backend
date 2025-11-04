package com.pedi2t.pedi2t.Service;

import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.LoginResponseDTO; // ¡Necesitás crear este DTO!

public interface UsuarioLoginService {
    
    LoginResponseDTO login(UsuarioLoginDTO loginDTO);
    
}