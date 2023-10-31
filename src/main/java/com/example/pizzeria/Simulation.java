package com.example.pizzeria;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Simulation {
    private final DinersGenerator dinersGenerator;
    private boolean isRunning = false;

    public boolean start() {
        if (isRunning) {
            return false;
        }

        dinersGenerator.start();
        isRunning = true;

        return true;
    }

    public boolean pause() {
        if (!isRunning) {
            return false;
        }

        dinersGenerator.pause();
        isRunning = false;

        return false;
    }
}
