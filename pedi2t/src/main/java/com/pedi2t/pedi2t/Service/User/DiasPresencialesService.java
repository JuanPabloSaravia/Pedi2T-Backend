package com.pedi2t.pedi2t.Service.User;

import com.pedi2t.pedi2t.DTO.DiasPresencialesDTO;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;

public interface DiasPresencialesService {
    DiasPresencialesEntity actualizarDiasPresenciales(Long usuarioId, DiasPresencialesDTO diasDTO);
    DiasPresencialesDTO obtenerDiasPresenciales(Long usuarioId);
}