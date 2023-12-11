package com.example.pizzeria.controllers;

import com.example.pizzeria.dto.KitchenStateDto;
import com.example.pizzeria.services.ISimulationService;
import com.example.pizzeria.services.KitchenStateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    private final ISimulationService simulationService;
    private final KitchenStateService kitchenStateService;

    @PostMapping("start")
    @Operation(summary = "Start the simulation")
    public ResponseEntity<Map<String, String>> start() throws IllegalStateException {
        boolean started = simulationService.start();

        String responseMessage = "simulation " + (started ? "started" : "has already started");
        Map<String, String> response = new HashMap<>();
        response.put("message", responseMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("pause")
    @Operation(summary = "Pause the simulation")
    public ResponseEntity<Map<String, String>> pause() throws IllegalStateException {
        boolean paused = simulationService.pause();

        String responseMessage = "simulation " + (paused ? "paused" : "is currently not running");
        Map<String, String> response = new HashMap<>();
        response.put("message", responseMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("terminate")
    @Operation(summary = "Terminate the simulation")
    public ResponseEntity<KitchenStateDto> terminate() throws IllegalStateException {
        var kitchenState = kitchenStateService.getKitchenState();
        simulationService.terminate();
        return ResponseEntity.ok(kitchenState);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error accessing unconfigured simulation");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}
