package com.pedi2t.pedi2t.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Enum.DiaSemana;

public interface MenuDiaRepository extends JpaRepository<MenuDiaEntity, Long> {
    List<MenuDiaEntity> findAllByPublicadoTrue();
    Optional<MenuDiaEntity> findByDiaSemana(DiaSemana diaSemana);
    List<MenuDiaEntity> findByDiaSemanaIn(List<DiaSemana> diasSemana);
}
