package com.pedi2t.pedi2t.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.JwtService;
import com.pedi2t.pedi2t.Service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;  

    @Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JwtService jwtService; // Dependencia del servicio de JWT

    @Override
    public UsuarioEntity registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        if (usuarioRepo.findByEmail(usuarioRegistroDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
    
    UsuarioEntity usuarioNuevo = new UsuarioEntity();

        usuarioNuevo.setNombre(usuarioRegistroDTO.getNombre());
        usuarioNuevo.setApellido(usuarioRegistroDTO.getApellido());
        usuarioNuevo.setEmail(usuarioRegistroDTO.getEmail());
        // Encriptar la contraseña antes de guardarla
        usuarioNuevo.setContrasena(passwordEncoder.encode(usuarioRegistroDTO.getContrasena()));
        usuarioNuevo.setDireccion(usuarioRegistroDTO.getDireccion());
        usuarioNuevo.setTelefono(usuarioRegistroDTO.getTelefono());
        usuarioNuevo.setRol("EMPLEADO");

        return usuarioRepo.save(usuarioNuevo);
    }
    

    @Override
    public LoginResponseDTO login(UsuarioLoginDTO loginDTO) {
        // 1. Buscar al usuario por correo
        UsuarioEntity usuario = usuarioRepo.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // 2. Verificar la contraseña
        if (!passwordEncoder.matches(loginDTO.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Generar el token JWT
        String token = jwtService.generateToken(usuario.getEmail());

        // 4. Devolver el DTO de respuesta con el token
        return new LoginResponseDTO(token);
    }

}
