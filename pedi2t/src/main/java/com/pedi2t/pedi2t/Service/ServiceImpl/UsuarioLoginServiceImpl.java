package com.pedi2t.pedi2t.Service.ServiceImpl;


import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.LoginResponseDTO; // ¡Asegurate de crear este DTO!
import com.pedi2t.pedi2t.Entity.UsuarioEntity; // (Usa tu entidad de usuario)
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.UsuarioLoginService;
import com.pedi2t.pedi2t.Service.JwtService; // ¡El servicio de JWT que creaste!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioLoginServiceImpl implements UsuarioLoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService; // Dependencia del servicio de JWT

    @Override
    public LoginResponseDTO login(UsuarioLoginDTO loginDTO) {
        // 1. Buscar al usuario por correo
        UsuarioEntity usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
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
