package com.pedi2t.pedi2t.Controller.User;


import com.pedi2t.pedi2t.DTO.HomeMenusResponseDTO;
import com.pedi2t.pedi2t.DTO.MenuDiaDTO;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.DiasPresencialesRepository;

import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.DTO.PlatoDTO;


@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:5173")
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    

    @Autowired
    private DiasPresencialesRepository diasPresencialesRepository;

    @Autowired
    private MenuDiaRepository menuDiaRepository;

    @Autowired
    private MenuPlatosRepository menuPlatosRepository;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<HomeMenusResponseDTO> getHomeUpdates(@PathVariable Long usuarioId) {
        // Requerimos que se pase el usuarioId para saber qué días configuró
        if (usuarioId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Obtener los días presenciales configurados por el usuario
        var diasOpt = diasPresencialesRepository.findByUsuarioId(usuarioId);
        if (diasOpt.isEmpty()) {
            // Si el usuario no configuró días, devolvemos lista vacía
            return ResponseEntity.ok(new HomeMenusResponseDTO(usuarioId, Collections.emptyList()));
        }
        var dias = diasOpt.get();

        // Obtener todos los menús publicados y filtrar por día de la semana según la configuración del usuario
        List<MenuDiaEntity> publishedMenus = menuDiaRepository.findAllByPublicadoTrue();

        LocalDate today = LocalDate.now();

        List<MenuDiaDTO> menus = publishedMenus.stream()
            // consideramos solo menús desde hoy en adelante
            .filter(md -> !md.getFecha().toLocalDate().isBefore(today))
            // filtrar por día de semana
            .filter(md -> {
                LocalDateTime fecha = md.getFecha();
                DayOfWeek dow = fecha.getDayOfWeek();
                return switch (dow) {
                    case MONDAY -> Boolean.TRUE.equals(dias.getLunes());
                    case TUESDAY -> Boolean.TRUE.equals(dias.getMartes());
                    case WEDNESDAY -> Boolean.TRUE.equals(dias.getMiercoles());
                    case THURSDAY -> Boolean.TRUE.equals(dias.getJueves());
                    case FRIDAY -> Boolean.TRUE.equals(dias.getViernes());
                    default -> false;
                };
            })
            .map(md -> {
                // Obtener platos del menú
                List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findByMenuDiaId(md.getId());
                List<PlatoDTO> platos = menuPlatos.stream().map(mp -> {
                    PlatoEntity p = mp.getPlato();
                    PlatoDTO dto = new PlatoDTO();
                    dto.setIdPlato(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setDescripcion(p.getDescripcion());
                    dto.setImagenUrl(p.getImagenUrl());
                    dto.setCategoria(p.getCategoria());
                    return dto;
                }).collect(Collectors.toList());

                return new MenuDiaDTO(md.getId(), md.getFecha().toString(), md.getDescripcion(), platos);
            })
            .collect(Collectors.toList());

        HomeMenusResponseDTO response = new HomeMenusResponseDTO(usuarioId, menus);
        return ResponseEntity.ok(response);
    }
}
