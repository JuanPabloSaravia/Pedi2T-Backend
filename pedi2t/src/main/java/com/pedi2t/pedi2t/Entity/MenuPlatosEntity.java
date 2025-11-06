package com.pedi2t.pedi2t.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "menu_platos")
public class MenuPlatosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El pedido debe estar asociado a un plato")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_plato", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_menu_platos_plato")
    )
    private PlatoEntity plato;

    @NotNull(message = "El pedido debe estar asociado a un menú del día")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_menu_dia", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_menu_platos_menu_dia")
    )
    private MenuDiaEntity menuDia;


}
