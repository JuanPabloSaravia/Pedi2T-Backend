package com.pedi2t.pedi2t.Repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pedi2t.pedi2t.Entity.MenuPlatosEntity;

public interface MenuPlatosRepository extends JpaRepository<MenuPlatosEntity, Long> {
    List<MenuPlatosEntity> findByMenuDiaId(Long menuDiaId);
    
    List<MenuPlatosEntity> findByMenuDiaIdAndPublicadoTrue(Long menuDiaId);
    
    List<MenuPlatosEntity> findByPublicadoTrue();
    
    List<MenuPlatosEntity> findByPlatoId(Long platoId);
    
    @Modifying
    @Query("UPDATE MenuPlatosEntity mp SET mp.publicado = false WHERE mp.publicado = true")
    void despublicarTodosLosMenuPlatos();
}
