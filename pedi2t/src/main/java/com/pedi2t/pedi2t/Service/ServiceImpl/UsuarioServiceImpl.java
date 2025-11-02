package com.pedi2t.pedi2t.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;  

    @Override
    public UsuarioEntity registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        if (usuarioRepo.findByEmail(usuarioRegistroDTO.getEmail()) != null) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
    
    UsuarioEntity usuarioNuevo = new UsuarioEntity();

        usuarioNuevo.setNombre(usuarioRegistroDTO.getNombre());
        usuarioNuevo.setApellido(usuarioRegistroDTO.getApellido()); // Agregado el mapeo del apellido
        usuarioNuevo.setEmail(usuarioRegistroDTO.getEmail());
        usuarioNuevo.setContrasena(usuarioRegistroDTO.getContrasena());
        usuarioNuevo.setDireccion(usuarioRegistroDTO.getDireccion());
        usuarioNuevo.setTelefono(usuarioRegistroDTO.getTelefono());
        usuarioNuevo.setRol("EMPLEADO");

        return usuarioRepo.save(usuarioNuevo);
    }

}
