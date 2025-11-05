package com.pedi2t.pedi2t.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;

public interface MenuPlatosRepository extends JpaRepository<MenuPlatosEntity, Long> {
    List<MenuPlatosEntity> findByMenuDiaId(Long menuDiaId);
}
