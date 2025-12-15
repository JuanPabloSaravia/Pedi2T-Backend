package com.pedi2t.pedi2t.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Enum.EstadoPedido;

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
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p JOIN FETCH p.usuario u " +
           "JOIN FETCH pd.plato pl " +
           "WHERE u.id = :usuarioId AND p.estado = 'ENTREGADO' " +
           "ORDER BY pd.fechaEntrega DESC")
    List<PedidoDia> findHistorialByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p " +
           "WHERE pd.fechaEntrega BETWEEN :fechaInicio AND :fechaFin " +
           "AND p.estado = 'PENDIENTE'")
    List<PedidoDia> findPedidosPendientesByFechaRange(
        @Param("fechaInicio") LocalDate fechaInicio, 
        @Param("fechaFin") LocalDate fechaFin
    );
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p " +
           "WHERE pd.fechaEntrega = :fechaEntrega AND p.estado = 'CONFIRMADO'")
    List<PedidoDia> findPedidosConfirmadosByFecha(@Param("fechaEntrega") LocalDate fechaEntrega);
    
    @Query("SELECT pd FROM PedidoDia pd JOIN FETCH pd.pedidoEntity p JOIN FETCH pd.plato " +
           "WHERE p.estado = :estado")
    List<PedidoDia> findPedidosByEstado(@Param("estado") EstadoPedido estado);
    
    @Query("SELECT pd.plato.id, pd.plato.nombre, pd.plato.categoria, pd.plato.imagenUrl, " +
           "SUM(CASE WHEN p.estado = 'PENDIENTE' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN p.estado = 'CONFIRMADO' THEN 1 ELSE 0 END) " +
           "FROM PedidoDia pd JOIN pd.pedidoEntity p " +
           "WHERE p.estado IN ('PENDIENTE', 'CONFIRMADO') " +
           "GROUP BY pd.plato.id, pd.plato.nombre, pd.plato.categoria, pd.plato.imagenUrl " +
           "ORDER BY (SUM(CASE WHEN p.estado = 'PENDIENTE' THEN 1 ELSE 0 END) + " +
           "         SUM(CASE WHEN p.estado = 'CONFIRMADO' THEN 1 ELSE 0 END)) DESC")
    List<Object[]> obtenerResumenPlatosPedidos();
    
    @Query("SELECT COUNT(pd) FROM PedidoDia pd JOIN pd.pedidoEntity p " +
           "WHERE p.estado IN ('PENDIENTE', 'CONFIRMADO')")
    Long obtenerTotalPedidosActivos();
}

