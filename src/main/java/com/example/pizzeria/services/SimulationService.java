package com.example.pizzeria.services;

import com.example.pizzeria.DinersGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimulationService {
    private final DinersGenerator dinersGenerator;
    private boolean isRunning = false;

    public boolean start() throws IllegalStateException {
        if (isRunning) {
            return false;
        }

        dinersGenerator.start();
        isRunning = true;

        return true;
    }

    public boolean pause() throws IllegalStateException {
        if (!isRunning) {
            return false;
        }

        dinersGenerator.pause();
        isRunning = false;

        return false;
    }
}
