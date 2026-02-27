package com.pico_placa_api.controller;

import com.pico_placa_api.controller.dto.ConsultaPicoPlacaRequest;
import com.pico_placa_api.controller.dto.ConsultaPicoPlacaResponse;
import com.pico_placa_api.model.Vehiculo;
import com.pico_placa_api.service.PicoPlacaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pico-placa")
//@CrossOrigin(origins = "*")
public class PicoPlacaController {

    private final PicoPlacaService picoPlacaService;

    public PicoPlacaController(PicoPlacaService picoPlacaService) {
        this.picoPlacaService = picoPlacaService;
    }

    @PostMapping("/consultar")
    public ResponseEntity<ConsultaPicoPlacaResponse> consultar(@Valid @RequestBody ConsultaPicoPlacaRequest request) {
        Vehiculo vehiculo = new Vehiculo(
                request.getPlaca(),
                request.getTipoVehiculo(),
                request.isExento()
        );

        boolean puedeCircular = picoPlacaService.puedeCircular(
                vehiculo,
                request.getFechaHora()
        );

        String mensaje = puedeCircular
                ? "El vehículo puede circular"
                : "El vehículo NO puede circular";

        ConsultaPicoPlacaResponse response = new ConsultaPicoPlacaResponse(
                request.getPlaca(),
                puedeCircular,
                mensaje
        );

        return ResponseEntity.ok(response);
    }
}