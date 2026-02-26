package com.pico_placa_api.controller.dto;

import com.pico_placa_api.model.TipoVehiculo;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class ConsultaPicoPlacaRequest {

    @NotBlank(message = "El número de placa es obligatorio")
    @Pattern(regexp = "^[A-Z]{3}-?\\d{3,4}$", message = "Formato de placa inválido. Use el formato ABC-1234")
    private String placa;

    @NotNull(message = "Debe especificar el tipo de vehículo")
    private TipoVehiculo tipoVehiculo;

    private boolean exento;

    @NotNull(message = "La fecha y hora son obligatorias")
    @FutureOrPresent(message = "La fecha y hora a consultar no puede estar en el pasado")
    private LocalDateTime fechaHora;

    // Getters y Setters

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public boolean isExento() {
        return exento;
    }

    public void setExento(boolean exento) {
        this.exento = exento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}