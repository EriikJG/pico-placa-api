package com.pico_placa_api.service;

import com.pico_placa_api.model.TipoVehiculo;
import com.pico_placa_api.model.Vehiculo;
import com.pico_placa_api.model.HistorialConsulta;
import com.pico_placa_api.repository.VehiculoRepository;
import com.pico_placa_api.repository.HistorialConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PicoPlacaServiceTest {

    private VehiculoRepository vehiculoRepository;
    private HistorialConsultaRepository historialRepository;
    private PicoPlacaService service;

    @BeforeEach
    void setUp() {
        vehiculoRepository = mock(VehiculoRepository.class);
        historialRepository = mock(HistorialConsultaRepository.class);
        service = new PicoPlacaService(vehiculoRepository, historialRepository);

        // 1. CONFIGURACIÓN CLAVE: Por defecto, simulamos que el vehículo NO existe
        when(vehiculoRepository.existsByPlacaValor(anyString())).thenReturn(false);

        // Configuramos los retornos de los saves para evitar NullPointer si los usas
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(i -> i.getArguments()[0]);
        when(historialRepository.save(any(HistorialConsulta.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void debeGuardarVehiculoSoloSiNoExiste() {
        // DADO
        Vehiculo vehiculo = new Vehiculo("ABC-1234");
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);

        // Simulamos que ya existe en la base de datos
        when(vehiculoRepository.existsByPlacaValor("ABC-1234")).thenReturn(true);

        // CUANDO
        service.puedeCircular(vehiculo, fechaHora);

        // ENTONCES: Verificamos que save() NUNCA se llamó para el vehículo
        verify(vehiculoRepository, times(0)).save(any(Vehiculo.class));
        // Pero el historial SIEMPRE debe guardarse
        verify(historialRepository, times(1)).save(any(HistorialConsulta.class));
    }

    @Test
    void debeGuardarVehiculoSiEsLaPrimeraVez() {
        // DADO
        Vehiculo vehiculo = new Vehiculo("XYZ-9999");
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);

        // Simulamos que NO existe
        when(vehiculoRepository.existsByPlacaValor("XYZ-9999")).thenReturn(false);

        // CUANDO
        service.puedeCircular(vehiculo, fechaHora);

        // ENTONCES: Verificamos que se llamó al save exactamente una vez
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void noDebePermitirCircular_siEsLunes_enHorarioRestringido_yPlacaTerminaEn1() {
        // DADO
        Vehiculo vehiculo = new Vehiculo("ABC-1231");
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 2, 7, 0); // lunes

        // CUANDO
        boolean puedeCircular = service.puedeCircular(vehiculo, fechaHora);

        // ENTONCES
        assertFalse(puedeCircular);
    }

    @Test
    void noDebePermitirCircular_siEsMartes_yPlacaTerminaEn3() {
        Vehiculo vehiculo = new Vehiculo("ABC-1233");
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 3, 8, 0); // martes

        boolean puedeCircular = service.puedeCircular(vehiculo, fechaHora);

        assertFalse(puedeCircular);
    }

    @Test
    void noDebePermitirCircular_siEsMiercoles_yPlacaTerminaEn5() {
        Vehiculo vehiculo = new Vehiculo("ABC-1235");
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 4, 17, 30); // miércoles

        boolean puedeCircular = service.puedeCircular(vehiculo, fechaHora);

        assertFalse(puedeCircular);
    }

    @Test
    void debePermitirCircular_fueraDeHorarioRestringido() {
        Vehiculo vehiculo = new Vehiculo("ABC-1231");
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 3, 10, 0);

        boolean puedeCircular = service.puedeCircular(vehiculo, fechaHora);

        assertTrue(puedeCircular);
    }

    @Test
    void noDebePermitirCircular_siEsMoto_yPlacaRestringida_enHorarioPico() {
        Vehiculo moto = new Vehiculo("AA-121A", TipoVehiculo.MOTOCICLETA);
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 2, 6, 30); // lunes

        boolean puedeCircular = service.puedeCircular(moto, fechaHora);

        assertFalse(puedeCircular);
    }

    @Test
    void debePermitirCircular_siVehiculoEstaExento() {
        Vehiculo vehiculo = new Vehiculo("ABC-1231", TipoVehiculo.AUTOMOVIL, true);
        LocalDateTime fechaHora = LocalDateTime.of(2026, 3, 2, 7, 30);

        boolean puedeCircular = service.puedeCircular(vehiculo, fechaHora);

        assertTrue(puedeCircular);
    }

    @Test
    void debeLanzarExcepcion_siFechaHoraEsAnteriorAActual() {
        Vehiculo vehiculo = new Vehiculo("ABC-1231");
        LocalDateTime fechaHoraPasada = LocalDateTime.of(2025, 1, 1, 7, 30);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.puedeCircular(vehiculo, fechaHoraPasada)
        );
    }
}