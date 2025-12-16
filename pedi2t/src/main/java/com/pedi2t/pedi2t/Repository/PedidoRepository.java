package com.pedi2t.pedi2t.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedi2t.pedi2t.Entity.PedidoEntity;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    
}
