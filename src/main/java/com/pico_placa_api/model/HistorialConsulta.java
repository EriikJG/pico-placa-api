package com.pico_placa_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_consultas")
public class HistorialConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private LocalDateTime fechaHoraConsulta;
    private boolean puedeCircular;

    public HistorialConsulta() {}

    public HistorialConsulta(String placa, LocalDateTime fechaHoraConsulta, boolean puedeCircular) {
        this.placa = placa;
        this.fechaHoraConsulta = fechaHoraConsulta;
        this.puedeCircular = puedeCircular;
    }

    // Getters
    public Long getId() { return id; }
    public String getPlaca() { return placa; }
    public LocalDateTime getFechaHoraConsulta() { return fechaHoraConsulta; }
    public boolean isPuedeCircular() { return puedeCircular; }
}