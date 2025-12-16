package com.pedi2t.pedi2t.Service.Admin.ServiceImpl;

import com.pedi2t.pedi2t.DTO.Admin.HomeAdminResponseDTO;
import com.pedi2t.pedi2t.DTO.Admin.MenuDiaAdminDTO;
import com.pedi2t.pedi2t.DTO.Admin.PlatoAdminDTO;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.MenuPlatosRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.Admin.HomeAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeAdminServiceImpl implements HomeAdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MenuDiaRepository menuDiaRepository;

    @Autowired
    private MenuPlatosRepository menuPlatosRepository;

    @Override
    public HomeAdminResponseDTO obtenerMenusParaAdmin(Long usuarioId) {
        // Validar usuario
        if (usuarioId == null) {
            throw new IllegalArgumentException("El usuarioId no puede ser nulo");
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + usuarioId);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        
        // Validar que sea admin
        if (!"ADMIN".equals(usuario.getRol())) {
            throw new SecurityException("Solo los administradores pueden acceder a esta funcionalidad");
        }

        // Obtener todos los menús (publicados y no publicados)
        List<MenuDiaEntity> todosLosMenus = menuDiaRepository.findAll();
        
        List<MenuDiaAdminDTO> menus = todosLosMenus.stream()
            .map(this::convertirAMenuDiaAdminDTO)
            .collect(Collectors.toList());

        return new HomeAdminResponseDTO(usuarioId, menus);
    }

    private MenuDiaAdminDTO convertirAMenuDiaAdminDTO(MenuDiaEntity menuDia) {
        List<MenuPlatosEntity> menuPlatos = menuPlatosRepository.findByMenuDiaId(menuDia.getId());
        List<PlatoAdminDTO> platos = menuPlatos.stream()
            .map(this::convertirAPlatoAdminDTO)
            .collect(Collectors.toList());

        return new MenuDiaAdminDTO(
            menuDia.getId(), 
            menuDia.getDescripcion(), 
            menuDia.getDiaSemana(),
            platos
        );
    }

    private PlatoAdminDTO convertirAPlatoAdminDTO(MenuPlatosEntity menuPlatos) {
        PlatoEntity plato = menuPlatos.getPlato();
        PlatoAdminDTO dto = new PlatoAdminDTO();
        dto.setIdPlato(plato.getId());
        dto.setIdMenuPlato(menuPlatos.getId()); // Agregar el ID del MenuPlatosEntity
        dto.setNombre(plato.getNombre());
        dto.setDescripcion(plato.getDescripcion());
        dto.setImagenUrl(plato.getImagenUrl());
        dto.setCategoria(plato.getCategoria());
        dto.setPublicado(menuPlatos.getPublicado()); // Mostrar estado de publicado
        return dto;
    }
}