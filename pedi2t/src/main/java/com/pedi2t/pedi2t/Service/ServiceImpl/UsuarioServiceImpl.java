package com.pedi2t.pedi2t.Service.ServiceImpl;



import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.DTO.UsuarioResponseDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Repository.DiasPresencialesRepository;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;
import com.pedi2t.pedi2t.Service.JwtService;
import com.pedi2t.pedi2t.Service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;  

    @Autowired
    private DiasPresencialesRepository diasPresencialesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JwtService jwtService; // Dependencia del servicio de JWT

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
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

        // Guardar usuario primero para obtener id
        UsuarioEntity saved = usuarioRepo.save(usuarioNuevo);

        // Procesar días presenciales recibidos en el DTO (array de strings)
        if (usuarioRegistroDTO.getDiasPresenciales() != null && !usuarioRegistroDTO.getDiasPresenciales().isEmpty()) {
            DiasPresencialesEntity dias = new DiasPresencialesEntity();
            dias.setUsuario(saved);
            // Inicializar en false por defecto
            dias.setLunes(false);
            dias.setMartes(false);
            dias.setMiercoles(false);
            dias.setJueves(false);
            dias.setViernes(false);

            for (String d : usuarioRegistroDTO.getDiasPresenciales()) {
                if (d == null) continue;
                String lower = d.trim().toLowerCase();
                switch (lower) {
                    case "lunes":
                        dias.setLunes(true);
                        break;
                    case "martes":
                        dias.setMartes(true);
                        break;
                    case "miercoles":
                        dias.setMiercoles(true);
                        break;
                    case "jueves":
                        dias.setJueves(true);
                        break;
                    case "viernes":
                        dias.setViernes(true);
                        break;
                    default:
                        // ignorar valores desconocidos o podríamos lanzar excepción
                        break;
                }
            }

            diasPresencialesRepository.save(dias);
        }
         UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuarioNuevo.getId());
        usuarioResponseDTO.setDiasPresenciales(new ArrayList<>(usuarioRegistroDTO.getDiasPresenciales()));

        return usuarioResponseDTO;
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
