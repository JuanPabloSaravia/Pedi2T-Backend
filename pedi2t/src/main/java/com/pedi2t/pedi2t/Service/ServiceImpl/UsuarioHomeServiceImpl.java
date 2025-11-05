package com.pedi2t.pedi2t.Service.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.PlatoDTO;
import com.pedi2t.pedi2t.DTO.UsuarioHomeDTO;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.PlatoRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.UsuarioHomeService;

@Service
public class UsuarioHomeServiceImpl implements UsuarioHomeService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PlatoRepository platoRepo;

    @Override
    public UsuarioHomeDTO obtenerDatosHome(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El idUsuario no puede ser nulo");
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepo.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + idUsuario);
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Traemos los platos disponibles
        List<PlatoEntity> platos = platoRepo.findAll();

        // Convertimos los platos a DTOs
    List<PlatoDTO> platoDTOs = platos.stream()
        .map(plato -> {
            PlatoDTO dto = new PlatoDTO();
            dto.setIdPlato(plato.getId());
            dto.setNombre(plato.getNombre());
            dto.setDescripcion(plato.getDescripcion());
            dto.setImagenUrl(plato.getImagenUrl());
            dto.setCategoria(plato.getCategoria());
            return dto;
        })
        .collect(Collectors.toList());

        // DTO del home con los datos del usuario y los platos
        UsuarioHomeDTO homeDTO = new UsuarioHomeDTO();
        homeDTO.setId(usuario.getId());
        
        homeDTO.setPlatos(platoDTOs);

        return homeDTO;
    }
}

