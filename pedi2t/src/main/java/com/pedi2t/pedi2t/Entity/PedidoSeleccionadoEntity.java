package com.pedi2t.pedi2t.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pedidos_seleccionados")
public class PedidoSeleccionadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "fecha_seleccionada", nullable = false, updatable = false)
    private LocalDateTime fechaSeleccionada;

    @NotNull(message = "El pedido debe estar asociado a un usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_pedidos_usuarios")
    )
    private UsuarioEntity usuario;

    @NotNull(message = "El pedido debe estar asociado a un pedido")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_pedido", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_pedidos_seleccionados_usuarios")
    )
    private PedidoEntity pedido;

    @NotNull(message = "El pedido debe estar asociado a un menu del dia")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_menu_dia", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_menu_dia_usuarios")
    )
    private MenuDiaEntity menuDia;
}
