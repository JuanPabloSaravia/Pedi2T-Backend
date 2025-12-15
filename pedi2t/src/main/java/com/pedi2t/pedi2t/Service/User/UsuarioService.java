package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.ActualizarPerfilDTO;
import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.PedidosRealizadosResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioPerfilResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.DTO.UsuarioResponseDTO;


public interface UsuarioService {
    
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO);
    public LoginResponseDTO login(UsuarioLoginDTO loginDTO);
    public PedidosRealizadosResponseDTO obtenerPedidosProximaSemana(Long usuarioId);
    public UsuarioPerfilResponseDTO obtenerPerfilUsuario(Long usuarioId);
    public UsuarioPerfilResponseDTO actualizarPerfilUsuario(Long usuarioId, ActualizarPerfilDTO actualizarDTO);

}
