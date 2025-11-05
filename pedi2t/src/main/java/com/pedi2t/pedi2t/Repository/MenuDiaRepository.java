package com.pedi2t.pedi2t.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedi2t.pedi2t.Entity.MenuDiaEntity;

public interface MenuDiaRepository extends JpaRepository<MenuDiaEntity, Long> {
    List<MenuDiaEntity> findAllByPublicadoTrue();
}
