package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.ActualizarPerfilDTO;
import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioPerfilResponseDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.DTO.UsuarioResponseDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.User.JwtService;
import com.pedi2t.pedi2t.Service.User.UsuarioService;
import com.pedi2t.pedi2t.Repository.DiasPresencialesRepository;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;

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
                if (d == null)
                    continue;
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
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setId(usuario.getId());
        responseDTO.setToken(token);
        responseDTO.setNombre(usuario.getNombre());
        responseDTO.setApellido(usuario.getApellido());
        responseDTO.setEmail(usuario.getEmail());
        responseDTO.setDireccion(usuario.getDireccion());
        responseDTO.setTelefono(usuario.getTelefono());
        responseDTO.setRol(usuario.getRol());
        return responseDTO;
    }

    @Override
    public UsuarioPerfilResponseDTO obtenerPerfilUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        UsuarioPerfilResponseDTO perfil = new UsuarioPerfilResponseDTO();
        perfil.setId(usuario.getId());
        perfil.setNombre(usuario.getNombre());
        perfil.setApellido(usuario.getApellido());
        perfil.setEmail(usuario.getEmail());
        perfil.setDireccion(usuario.getDireccion());
        perfil.setTelefono(usuario.getTelefono());
        perfil.setRol(usuario.getRol());

        // Intentar obtener dias presenciales si existen
        diasPresencialesRepository.findByUsuarioId(usuarioId).ifPresent(dias -> {
            ArrayList<String> diasList = new ArrayList<>();
            if (Boolean.TRUE.equals(dias.getLunes())) diasList.add("LUNES");
            if (Boolean.TRUE.equals(dias.getMartes())) diasList.add("MARTES");
            if (Boolean.TRUE.equals(dias.getMiercoles())) diasList.add("MIERCOLES");
            if (Boolean.TRUE.equals(dias.getJueves())) diasList.add("JUEVES");
            if (Boolean.TRUE.equals(dias.getViernes())) diasList.add("VIERNES");
            perfil.setDiasPresenciales(diasList);
        });

        return perfil;
    }

    @Override
    public UsuarioPerfilResponseDTO actualizarPerfilUsuario(Long usuarioId, ActualizarPerfilDTO actualizarDTO) {
        // 1. Validar que el usuario existe
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        // 2. Actualizar solo los campos editables
        usuario.setNombre(actualizarDTO.getNombre());
        usuario.setApellido(actualizarDTO.getApellido());
        usuario.setDireccion(actualizarDTO.getDireccion());
        usuario.setTelefono(actualizarDTO.getTelefono());

        // 3. Persista los cambios en la BD
        UsuarioEntity usuarioActualizado = usuarioRepo.save(usuario);

        // 4. Mapear a DTO y devolver
        UsuarioPerfilResponseDTO perfil = new UsuarioPerfilResponseDTO();
        perfil.setId(usuarioActualizado.getId());
        perfil.setNombre(usuarioActualizado.getNombre());
        perfil.setApellido(usuarioActualizado.getApellido());
        perfil.setEmail(usuarioActualizado.getEmail());
        perfil.setDireccion(usuarioActualizado.getDireccion());
        perfil.setTelefono(usuarioActualizado.getTelefono());
        perfil.setRol(usuarioActualizado.getRol());

        // Intentar obtener dias presenciales si existen
        diasPresencialesRepository.findByUsuarioId(usuarioId).ifPresent(dias -> {
            ArrayList<String> diasList = new ArrayList<>();
            if (Boolean.TRUE.equals(dias.getLunes())) diasList.add("LUNES");
            if (Boolean.TRUE.equals(dias.getMartes())) diasList.add("MARTES");
            if (Boolean.TRUE.equals(dias.getMiercoles())) diasList.add("MIERCOLES");
            if (Boolean.TRUE.equals(dias.getJueves())) diasList.add("JUEVES");
            if (Boolean.TRUE.equals(dias.getViernes())) diasList.add("VIERNES");
            perfil.setDiasPresenciales(diasList);
        });

        return perfil;
    }
}
