package com.pedi2t.pedi2t.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pedi2t.pedi2t.Entity.PedidoDia;

public interface PedidoDiaRepository extends JpaRepository<PedidoDia, Long> {
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p JOIN FETCH p.usuario u " +
           "JOIN FETCH pd.plato pl JOIN FETCH pd.menuDia md " +
           "WHERE u.id = :usuarioId AND pd.fechaEntrega BETWEEN :fechaInicio AND :fechaFin " +
           "AND p.estado = 'PENDIENTE' " +
           "ORDER BY pd.fechaEntrega")
    List<PedidoDia> findByUsuarioIdAndFechaEntregaBetween(
        @Param("usuarioId") Long usuarioId, 
        @Param("fechaInicio") LocalDate fechaInicio, 
        @Param("fechaFin") LocalDate fechaFin
    );
    
    @Query("SELECT COUNT(pd) > 0 FROM PedidoDia pd JOIN pd.pedidoEntity p " +
           "WHERE p.usuario.id = :usuarioId AND pd.fechaEntrega = :fechaEntrega " +
           "AND p.estado = 'PENDIENTE'")
    boolean existePedidoPendienteEnFecha(
        @Param("usuarioId") Long usuarioId, 
        @Param("fechaEntrega") LocalDate fechaEntrega
    );
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p " +
           "JOIN FETCH pd.menuDia md JOIN FETCH pd.plato pl " +
           "WHERE p.id = :pedidoId")
    Optional<PedidoDia> findByPedidoEntityId(@Param("pedidoId") Long pedidoId);
}
