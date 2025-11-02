package com.pedi2t.pedi2t.Service;

import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;

public interface UsuarioService {
    
    public UsuarioEntity registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO);

}
