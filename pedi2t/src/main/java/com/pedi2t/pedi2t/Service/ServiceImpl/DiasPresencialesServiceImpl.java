package com.pedi2t.pedi2t.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.DiasPresencialesDTO;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Repository.DiasPresencialesRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.DiasPresencialesService;

import jakarta.transaction.Transactional;

@Service
public class DiasPresencialesServiceImpl implements DiasPresencialesService {

    @Autowired
    private DiasPresencialesRepository diasPresencialesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public DiasPresencialesEntity actualizarDiasPresenciales(Long usuarioId, DiasPresencialesDTO diasDTO) {
        // Validaciones de entrada
        if (usuarioId == null) {
            throw new IllegalArgumentException("El id de usuario no puede ser null");
        }
        if (diasDTO == null) {
            throw new IllegalArgumentException("Los días presenciales no pueden ser nulos");
        }

        // 1) Asegurarse de que el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        // 2) Obtener los dias presenciales si existen, o crear uno nuevo asociado al usuario
        DiasPresencialesEntity dias = diasPresencialesRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    DiasPresencialesEntity nuevo = new DiasPresencialesEntity();
                    nuevo.setUsuario(usuario);
                    return nuevo;
                });

    // Actualizar los días (usar Boolean.TRUE.equals para evitar nulls)
    dias.setLunes(Boolean.TRUE.equals(diasDTO.getLunes()));
    dias.setMartes(Boolean.TRUE.equals(diasDTO.getMartes()));
    dias.setMiercoles(Boolean.TRUE.equals(diasDTO.getMiercoles()));
    dias.setJueves(Boolean.TRUE.equals(diasDTO.getJueves()));
    dias.setViernes(Boolean.TRUE.equals(diasDTO.getViernes()));

        return diasPresencialesRepository.save(dias);
    }

    @Override
    public DiasPresencialesDTO obtenerDiasPresenciales(Long usuarioId) {
        DiasPresencialesEntity dias = diasPresencialesRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontraron días presenciales para el usuario: " + usuarioId));

        DiasPresencialesDTO dto = new DiasPresencialesDTO();
        dto.setLunes(dias.getLunes());
        dto.setMartes(dias.getMartes());
        dto.setMiercoles(dias.getMiercoles());
        dto.setJueves(dias.getJueves());
        dto.setViernes(dias.getViernes());

        return dto;
    }
}