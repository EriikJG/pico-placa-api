package com.pico_placa_api.controller;

import com.pico_placa_api.controller.dto.ConsultaPicoPlacaRequest;
import com.pico_placa_api.controller.dto.ConsultaPicoPlacaResponse;
import com.pico_placa_api.model.Vehiculo;
import com.pico_placa_api.service.PicoPlacaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pico-placa")
//@CrossOrigin(origins = "*")
public class PicoPlacaController {

    private final PicoPlacaService picoPlacaService;

    public PicoPlacaController(PicoPlacaService picoPlacaService) {
        this.picoPlacaService = picoPlacaService;
    }

    @PostMapping("/consultar")
    public ResponseEntity<?> consultar(@Valid @RequestBody ConsultaPicoPlacaRequest request) {
        try {
            // 1. Mapeo de Request a Modelo de Dominio
            Vehiculo vehiculo = new Vehiculo(
                    request.getPlaca(),
                    request.getTipoVehiculo(),
                    request.isExento()
            );

            // 2. Ejecución de la lógica de negocio y persistencia en Supabase
            boolean puedeCircular = picoPlacaService.puedeCircular(
                    vehiculo,
                    request.getFechaHora()
            );

            // 3. Preparación de la respuesta exitosa
            String mensaje = puedeCircular
                    ? "El vehículo puede circular"
                    : "El vehículo NO puede circular";

            ConsultaPicoPlacaResponse response = new ConsultaPicoPlacaResponse(
                    request.getPlaca(),
                    puedeCircular,
                    mensaje
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Capturamos el error de "fecha pasada" y devolvemos 400 Bad Request
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Validación fallida",
                            "mensaje", e.getMessage()
                    ));
        } catch (Exception e) {
            // Capturamos errores de conexión a base de datos u otros inesperados
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Error interno",
                            "mensaje", "No se pudo procesar la consulta en este momento"
                    ));
        }
    }
}