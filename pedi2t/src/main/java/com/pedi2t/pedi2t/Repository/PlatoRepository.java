package com.pedi2t.pedi2t.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pedi2t.pedi2t.Entity.PlatoEntity;

@Repository
public interface PlatoRepository extends JpaRepository<PlatoEntity, Long> {
    // si más adelante querés buscar por categoría o por nombre, podés agregar métodos aquí
}
