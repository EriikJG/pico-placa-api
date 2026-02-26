package com.pico_placa_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Placa {

    @Column(name = "placa_valor")
    private String valor;

    // Constructor vacío obligatorio para JPA
    public Placa() {}

    public Placa(String valor) {
        this.valor = valor;
    }

    public int obtenerDigitoRestriccion(TipoVehiculo tipo) {
        if (tipo == TipoVehiculo.MOTOCICLETA) {
            return Character.getNumericValue(valor.charAt(valor.length() - 2));
        }
        return Character.getNumericValue(valor.charAt(valor.length() - 1));
    }

    public String getValor() {
        return valor;
    }
}