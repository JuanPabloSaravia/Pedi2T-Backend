package com.pedi2t.pedi2t.Enum;

public enum EstadoPedido {
    PENDIENTE("PENDIENTE"),
    CONFIRMADO("CONFIRMADO"), 
    CANCELADO("CANCELADO");

    private final String valor;

    EstadoPedido(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}