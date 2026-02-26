package com.pico_placa_api.service;

import com.pico_placa_api.model.HistorialConsulta;
import com.pico_placa_api.model.Vehiculo;
import com.pico_placa_api.repository.HistorialConsultaRepository;
import com.pico_placa_api.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

@Service
public class PicoPlacaService {

    private final VehiculoRepository vehiculoRepository;
    private final HistorialConsultaRepository historialRepository; // 1. Inyectamos el nuevo repo

    // Constructor actualizado para ambos repositorios
    public PicoPlacaService(VehiculoRepository vehiculoRepository,
                            HistorialConsultaRepository historialRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.historialRepository = historialRepository;
    }

    private static final Map<DayOfWeek, Set<Integer>> RESTRICCIONES = Map.of(
            DayOfWeek.MONDAY, Set.of(1, 2),
            DayOfWeek.TUESDAY, Set.of(3, 4),
            DayOfWeek.WEDNESDAY, Set.of(5, 6),
            DayOfWeek.THURSDAY, Set.of(7, 8),
            DayOfWeek.FRIDAY, Set.of(9, 0)
    );

    public boolean puedeCircular(Vehiculo vehiculo, LocalDateTime fechaHora) {
        validarFechaNoPasada(fechaHora);

        String placaValor = vehiculo.getPlaca().getValor();

        if (!vehiculoRepository.existsByPlacaValor(placaValor)) {
            vehiculoRepository.save(vehiculo);
        }

        // 2. Cálculo de la lógica
        boolean puedeCircular = calcularResultado(vehiculo, fechaHora);

        // 3. REGISTRO SIEMPRE: El historial se guarda en cada consulta
        historialRepository.save(new HistorialConsulta(
                placaValor,
                fechaHora,
                puedeCircular
        ));

        return puedeCircular;
    }

    // Encapsulamos la lógica para poder guardar el resultado antes de retornarlo
    private boolean calcularResultado(Vehiculo vehiculo, LocalDateTime fechaHora) {
        if (vehiculo.estaExento()) {
            return true;
        }

        if (!estaEnHorarioRestringido(fechaHora)) {
            return true;
        }

        Set<Integer> restringidos = RESTRICCIONES.get(fechaHora.getDayOfWeek());

        if (restringidos == null) {
            return true; // Sábado - Domingo
        }

        int digito = vehiculo.getPlaca().obtenerDigitoRestriccion(vehiculo.getTipo());

        return !restringidos.contains(digito);
    }

    private boolean estaEnHorarioRestringido(LocalDateTime fechaHora) {
        LocalTime hora = fechaHora.toLocalTime();
        return (!hora.isBefore(LocalTime.of(6, 0)) && !hora.isAfter(LocalTime.of(9, 30)))
                || (!hora.isBefore(LocalTime.of(16, 0)) && !hora.isAfter(LocalTime.of(20, 0)));
    }

    private void validarFechaNoPasada(LocalDateTime fechaHora) {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser anteriores a la actual");
        }
    }
}