package com.pico_placa_api.repository;

import com.pico_placa_api.model.HistorialConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialConsultaRepository extends JpaRepository<HistorialConsulta, Long> {
}