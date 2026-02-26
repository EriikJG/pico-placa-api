package com.pico_placa_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Placa placa;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;

    private boolean exento;

    // 1. Constructor vacío: Obligatorio para JPA/Hibernate
    public Vehiculo() {
    }

    public Vehiculo(String placaValor, TipoVehiculo tipo, boolean exento) {
        this.placa = new Placa(placaValor);
        this.tipo = tipo;
        this.exento = exento;
    }

    public Vehiculo(String placaValor, TipoVehiculo tipo) {
        this(placaValor, tipo, false);
    }

    public Vehiculo(String placaValor) {
        this(placaValor, TipoVehiculo.AUTOMOVIL, false);
    }

    // Getters
    public Placa getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public boolean estaExento() {
        return exento;
    }
}