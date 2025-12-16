package com.pedi2t.pedi2t.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedi2t.pedi2t.Entity.NotificacionEntity;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {

}
