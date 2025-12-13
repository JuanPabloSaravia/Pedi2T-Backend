package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.LoginResponseDTO;
import com.pedi2t.pedi2t.DTO.PedidoProximaSemanaDTO;
import com.pedi2t.pedi2t.DTO.PedidosProximaSemanaResponseDTO;
import com.pedi2t.pedi2t.DTO.PlatoDTO;
import com.pedi2t.pedi2t.DTO.UsuarioLoginDTO;
import com.pedi2t.pedi2t.DTO.UsuarioRegistroDTO;
import com.pedi2t.pedi2t.DTO.UsuarioResponseDTO;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
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
    private MenuDiaRepository menuDiaRepository;

    @Autowired
    private MenuPlatosRepository menuPlatosRepository;

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
    public PedidosProximaSemanaResponseDTO obtenerPedidosProximaSemana(Long usuarioId) {
        // Validar que el usuario existe
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        // Obtener la fecha actual
        LocalDate hoy = LocalDate.now();
        
        // Determinar el día de la semana actual
        DayOfWeek diaActual = hoy.getDayOfWeek();
        
        // Calcular el próximo lunes
        LocalDate proximoLunes;
        if (diaActual == DayOfWeek.MONDAY) {
            // Si hoy es lunes, el próximo lunes es en 7 días
            proximoLunes = hoy.plusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else {
            // Si no es lunes, calcular el próximo lunes
            proximoLunes = hoy.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        
        // Calcular el próximo viernes (fin de la próxima semana)
        LocalDate proximoViernes = proximoLunes.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        // Obtener todos los menús del usuario para la próxima semana
        List<MenuDiaEntity> menusDiasProxSemana = menuDiaRepository.findAll()
                .stream()
                .filter(menu -> {
                    LocalDate fechaMenu = menu.getFecha().toLocalDate();
                    return !fechaMenu.isBefore(proximoLunes) && !fechaMenu.isAfter(proximoViernes)
                            && menu.getUsuario().getId().equals(usuarioId);
                })
                .collect(Collectors.toList());

        // Obtener todos los menú platos asociados a estos menús
        List<PedidoProximaSemanaDTO> pedidos = new ArrayList<>();
        
        for (MenuDiaEntity menuDia : menusDiasProxSemana) {
            List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findByMenuDiaId(menuDia.getId());
            
            for (MenuPlatosEntity menuPlato : menuPlatos) {
                PedidoProximaSemanaDTO pedidoDTO = new PedidoProximaSemanaDTO();
                pedidoDTO.setMenuPlatoId(menuPlato.getId());
                pedidoDTO.setDiaSemana(menuDia.getDiaSemana());
                pedidoDTO.setMenuDiaDescripcion(menuDia.getDescripcion());
                
                // Convertir plato a DTO
                PlatoDTO platoDTO = new PlatoDTO();
                platoDTO.setIdPlato(menuPlato.getPlato().getId());
                platoDTO.setNombre(menuPlato.getPlato().getNombre());
                platoDTO.setDescripcion(menuPlato.getPlato().getDescripcion());
                platoDTO.setImagenUrl(menuPlato.getPlato().getImagenUrl());
                platoDTO.setCategoria(menuPlato.getPlato().getCategoria());
                pedidoDTO.setPlato(platoDTO);
                
                pedidos.add(pedidoDTO);
            }
        }

        // Armar la respuesta
        PedidosProximaSemanaResponseDTO response = new PedidosProximaSemanaResponseDTO();
        response.setUsuarioId(usuarioId);
        response.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
        response.setPedidos(pedidos);
        
        return response;
    }
}
