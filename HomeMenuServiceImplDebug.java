package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import com.pedi2t.pedi2t.DTO.HomeMenusResponseDTO;
import com.pedi2t.pedi2t.DTO.MenuDiaDTO;
import com.pedi2t.pedi2t.DTO.PlatoDTO;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.DiasPresencialesRepository;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.User.HomeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeMenuServiceImplDebug implements HomeMenuService {

    private static final Logger logger = LoggerFactory.getLogger(HomeMenuServiceImplDebug.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DiasPresencialesRepository diasPresencialesRepository;

    @Autowired
    private MenuDiaRepository menuDiaRepository;

    @Autowired
    private MenuPlatosRepository menuPlatosRepository;

    @Override
    public HomeMenusResponseDTO obtenerMenusUsuario(Long usuarioId) {
        logger.info("=== INICIANDO DEBUG PARA USUARIO ID: {} ===", usuarioId);
        
        // Validar usuario
        if (usuarioId == null) {
            logger.error("UsuarioId es nulo");
            throw new IllegalArgumentException("El usuarioId no puede ser nulo");
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            logger.error("Usuario no encontrado para id: {}", usuarioId);
            throw new IllegalArgumentException("Usuario no encontrado para id: " + usuarioId);
        }
        logger.info("✓ Usuario encontrado: {}", usuarioOpt.get().getEmail());

        // Obtener configuración de días presenciales
        Optional<DiasPresencialesEntity> diasOpt = diasPresencialesRepository.findByUsuarioId(usuarioId);
        if (diasOpt.isEmpty()) {
            logger.warn("❌ PROBLEMA: Usuario {} no tiene días presenciales configurados", usuarioId);
            return new HomeMenusResponseDTO(usuarioId, Collections.emptyList());
        }
        
        DiasPresencialesEntity dias = diasOpt.get();
        logger.info("✓ Días presenciales encontrados: L:{}, M:{}, X:{}, J:{}, V:{}", 
            dias.getLunes(), dias.getMartes(), dias.getMiercoles(), dias.getJueves(), dias.getViernes());

        // Verificar si tiene al menos un día configurado como presencial
        boolean tieneAlgunDiaPresencial = Boolean.TRUE.equals(dias.getLunes()) || 
                                        Boolean.TRUE.equals(dias.getMartes()) || 
                                        Boolean.TRUE.equals(dias.getMiercoles()) || 
                                        Boolean.TRUE.equals(dias.getJueves()) || 
                                        Boolean.TRUE.equals(dias.getViernes());
        
        if (!tieneAlgunDiaPresencial) {
            logger.warn("❌ PROBLEMA: Usuario {} no tiene ningún día marcado como presencial", usuarioId);
            return new HomeMenusResponseDTO(usuarioId, Collections.emptyList());
        }

        // Obtener menús publicados y filtrarlos
        List<MenuDiaEntity> publishedMenus = menuDiaRepository.findAllByPublicadoTrue();
        logger.info("✓ Menús publicados encontrados: {}", publishedMenus.size());
        
        if (publishedMenus.isEmpty()) {
            logger.warn("❌ PROBLEMA: No hay menús publicados en la base de datos");
            return new HomeMenusResponseDTO(usuarioId, Collections.emptyList());
        }

        // Log detallado de cada menú
        for (MenuDiaEntity menu : publishedMenus) {
            logger.info("  - Menú ID {}: {} ({})", menu.getId(), menu.getDescripcion(), menu.getDiaSemana());
        }

        List<MenuDiaDTO> menus = publishedMenus.stream()
            .filter(md -> {
                boolean esPresencial = esDiaPresencial(md, dias);
                logger.info("  Menú {} ({}): Es día presencial? {}", 
                    md.getId(), md.getDiaSemana(), esPresencial);
                return esPresencial;
            })
            .map(this::convertirAMenuDiaDTO)
            .collect(Collectors.toList());

        logger.info("✓ Menús finales después del filtro: {}", menus.size());
        
        if (menus.isEmpty()) {
            logger.warn("❌ PROBLEMA: No hay coincidencia entre menús publicados y días presenciales del usuario");
        }

        return new HomeMenusResponseDTO(usuarioId, menus);
    }

    private boolean esDiaPresencial(MenuDiaEntity menuDia, DiasPresencialesEntity dias) {
        String diaSemana = menuDia.getDiaSemana();
        if (diaSemana == null) {
            logger.warn("  ⚠️ Menú {} tiene diaSemana nulo", menuDia.getId());
            return false;
        }
        
        // Normalizar el día de la semana (puede venir en MAYÚSCULAS o minúsculas)
        String diaUpper = diaSemana.trim().toUpperCase();
        
        boolean resultado = switch (diaUpper) {
            case "LUNES" -> Boolean.TRUE.equals(dias.getLunes());
            case "MARTES" -> Boolean.TRUE.equals(dias.getMartes());
            case "MIERCOLES", "MIÉRCOLES" -> Boolean.TRUE.equals(dias.getMiercoles());
            case "JUEVES" -> Boolean.TRUE.equals(dias.getJueves());
            case "VIERNES" -> Boolean.TRUE.equals(dias.getViernes());
            default -> {
                logger.warn("  ⚠️ Día de semana no reconocido: '{}'", diaUpper);
                yield false;
            }
        };
        
        logger.debug("    Día: {} -> Presencial: {}", diaUpper, resultado);
        return resultado;
    }

    private MenuDiaDTO convertirAMenuDiaDTO(MenuDiaEntity menuDia) {
        logger.info("  Convirtiendo menú {} a DTO", menuDia.getId());
        
        // Obtener directamente solo los platos publicados desde el repository
        List<MenuPlatosEntity> menuPlatosPublicados = menuPlatosRepository.findByMenuDiaIdAndPublicadoTrue(menuDia.getId());
        logger.info("    Platos publicados encontrados: {}", menuPlatosPublicados.size());
        
        if (menuPlatosPublicados.isEmpty()) {
            logger.warn("    ❌ PROBLEMA: Menú {} no tiene platos publicados", menuDia.getId());
        }
        
        List<PlatoDTO> platos = menuPlatosPublicados.stream()
            .map(this::convertirAPlatoDTO)
            .collect(Collectors.toList());

        MenuDiaDTO dto = new MenuDiaDTO(
            menuDia.getId(), 
            menuDia.getFecha().toString(), 
            menuDia.getDescripcion(), 
            platos
        );
        
        logger.info("    DTO creado con {} platos", platos.size());
        return dto;
    }

    private PlatoDTO convertirAPlatoDTO(MenuPlatosEntity menuPlatos) {
        PlatoEntity plato = menuPlatos.getPlato();
        if (plato == null) {
            logger.error("    ❌ MenuPlatos {} tiene plato nulo", menuPlatos.getId());
            return null;
        }
        
        PlatoDTO dto = new PlatoDTO();
        dto.setIdPlato(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setDescripcion(plato.getDescripcion());
        dto.setImagenUrl(plato.getImagenUrl());
        dto.setCategoria(plato.getCategoria());
        
        logger.debug("      Plato convertido: {} - {}", dto.getIdPlato(), dto.getNombre());
        return dto;
    }
}