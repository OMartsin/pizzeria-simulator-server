package com.example.pizzeria.controllers;

import com.example.pizzeria.Initializer;
import com.example.pizzeria.services.ISimulationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    private final ISimulationService simulationService;
    private final Initializer initializer;

    @GetMapping("start")
    @Operation(summary = "Start the simulation")
    public ResponseEntity<Map<String, String>> start() throws IllegalStateException {
        initializer.init();
        boolean started = simulationService.start();

        String responseMessage = "simulation " + (started ? "started" : "has already started");
        Map<String, String> response = new HashMap<>();
        response.put("message", responseMessage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("pause")
    @Operation(summary = "Pause the simulation")
    public ResponseEntity<Map<String, String>> pause() throws IllegalStateException {
        boolean paused = simulationService.pause();

        String responseMessage = "simulation " + (paused ? "paused" : "is currently not running");
        Map<String, String> response = new HashMap<>();
        response.put("message", responseMessage);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error accessing unconfigured simulation");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}
