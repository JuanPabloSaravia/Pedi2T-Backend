package com.pedi2t.pedi2t.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pedi2t.pedi2t.Entity.DiasPresencialesEntity;



public interface DiasPresencialesRepository extends JpaRepository<DiasPresencialesEntity, Long> {
    
    Optional<DiasPresencialesEntity> findByUsuarioId(Long usuarioId);
}