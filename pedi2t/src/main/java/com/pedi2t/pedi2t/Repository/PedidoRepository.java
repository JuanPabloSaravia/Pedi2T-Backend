package com.pedi2t.pedi2t.Repository;

import com.pedi2t.pedi2t.Entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByUsuarioId(Long usuarioId);
}
