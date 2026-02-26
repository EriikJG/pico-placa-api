package com.pico_placa_api;

import jakarta.annotation.PostConstruct; // Importante para que @PostConstruct funcione
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone; // Importante para manejar la zona horaria

@SpringBootApplication
public class PicoPlacaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicoPlacaApiApplication.class, args);
    }
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        System.out.println("ZONA HORARIA CONFIGURADA: " + TimeZone.getDefault().getID());
    }
}