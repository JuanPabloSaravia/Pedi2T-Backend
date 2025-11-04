package com.pedi2t.pedi2t.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedi2t.pedi2t.Entity.UsuarioEntity;

@Repository

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

  Optional<UsuarioEntity> findByEmail(String email);
  

}
