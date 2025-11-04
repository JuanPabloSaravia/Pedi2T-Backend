package com.pedi2t.pedi2t.Service;

import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;

public interface UsuarioService {
    
    public UsuarioEntity registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO);
    public LoginResponseDTO login(UsuarioLoginDTO loginDTO);

}
