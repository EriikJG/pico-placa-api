package com.pico_placa_api.controller.dto;

public class ConsultaPicoPlacaResponse {

    private String placa;
    private boolean puedeCircular;
    private String mensaje;
    public ConsultaPicoPlacaResponse() { }
    public ConsultaPicoPlacaResponse(String placa,
                                     boolean puedeCircular,
                                     String mensaje) {
        this.placa = placa;
        this.puedeCircular = puedeCircular;
        this.mensaje = mensaje;
    }

    public String getPlaca() {
        return placa;
    }

    public boolean isPuedeCircular() {
        return puedeCircular;
    }

    public String getMensaje() {
        return mensaje;
    }
}