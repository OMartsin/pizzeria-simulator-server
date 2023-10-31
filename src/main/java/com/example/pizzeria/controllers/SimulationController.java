package com.example.pizzeria.controllers;

import com.example.pizzeria.Initializer;
import com.example.pizzeria.Simulation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    private final Simulation simulation;
    private final Initializer initializer;


    @GetMapping("start")
    public ResponseEntity<String> start() {
        initializer.init();
        boolean started = simulation.start();

        String responseString = "simulation " + (started ? "started" : "failed to start");
        return ResponseEntity.ok(responseString);
    }

    @GetMapping("pause")
    public ResponseEntity<String> pause() {
        boolean paused = simulation.pause();
        String responseString = "simulation " + (paused ? "paused" : "failed to pause");

        return ResponseEntity.ok(responseString);
    }

}
