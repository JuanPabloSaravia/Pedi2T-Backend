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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeMenuServiceImpl implements HomeMenuService {

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
        // Validar usuario
        if (usuarioId == null) {
            throw new IllegalArgumentException("El usuarioId no puede ser nulo");
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + usuarioId);
        }

        // Obtener configuración de días presenciales
        Optional<DiasPresencialesEntity> diasOpt = diasPresencialesRepository.findByUsuarioId(usuarioId);
        if (diasOpt.isEmpty()) {
            return new HomeMenusResponseDTO(usuarioId, Collections.emptyList());
        }
        DiasPresencialesEntity dias = diasOpt.get();

        // Obtener menús publicados y filtrarlos
        List<MenuDiaEntity> publishedMenus = menuDiaRepository.findAllByPublicadoTrue();
        
        List<MenuDiaDTO> menus = publishedMenus.stream()
            .filter(md -> esDiaPresencial(md, dias))
            .map(this::convertirAMenuDiaDTO)
            .collect(Collectors.toList());

        return new HomeMenusResponseDTO(usuarioId, menus);
    }

    private boolean esDiaPresencial(MenuDiaEntity menuDia, DiasPresencialesEntity dias) {
        String diaSemana = menuDia.getDiaSemana();
        if (diaSemana == null) {
            return false;
        }
        
        // Normalizar el día de la semana (puede venir en MAYÚSCULAS o minúsculas)
        String diaUpper = diaSemana.trim().toUpperCase();
        
        return switch (diaUpper) {
            case "LUNES" -> Boolean.TRUE.equals(dias.getLunes());
            case "MARTES" -> Boolean.TRUE.equals(dias.getMartes());
            case "MIERCOLES", "MIÉRCOLES" -> Boolean.TRUE.equals(dias.getMiercoles());
            case "JUEVES" -> Boolean.TRUE.equals(dias.getJueves());
            case "VIERNES" -> Boolean.TRUE.equals(dias.getViernes());
            default -> false;
        };
    }

    private MenuDiaDTO convertirAMenuDiaDTO(MenuDiaEntity menuDia) {
        List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findByMenuDiaId(menuDia.getId());
        List<PlatoDTO> platos = menuPlatos.stream()
            .map(this::convertirAPlatoDTO)
            .collect(Collectors.toList());

        return new MenuDiaDTO(
            menuDia.getId(), 
            menuDia.getFecha().toString(), 
            menuDia.getDescripcion(), 
            platos
        );
    }

    private PlatoDTO convertirAPlatoDTO(MenuPlatosEntity menuPlatos) {
        PlatoEntity plato = menuPlatos.getPlato();
        PlatoDTO dto = new PlatoDTO();
        dto.setIdPlato(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setDescripcion(plato.getDescripcion());
        dto.setImagenUrl(plato.getImagenUrl());
        dto.setCategoria(plato.getCategoria());
        return dto;
    }
}