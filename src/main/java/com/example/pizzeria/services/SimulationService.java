package com.example.pizzeria.services;

import com.example.pizzeria.DinersGenerator;
import com.example.pizzeria.Initializer;
import com.example.pizzeria.utilities.LoggerConfigUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationService implements ISimulationService {
    private final DinersGenerator dinersGenerator;
    private final Initializer initializer;
    private boolean isRunning = false;

    public boolean start() throws IllegalStateException {
        initializer.init();
        if (isRunning) {
            return false;
        }

        dinersGenerator.start();
        isRunning = true;

        LoggerConfigUtility.setNewLogFilename();

        return true;
    }

    public boolean pause() throws IllegalStateException {
        if (!isRunning) {
            return false;
        }

        dinersGenerator.pause();
        isRunning = false;

        return true;
    }

    public void terminate() throws IllegalStateException {
        initializer.terminate();
        isRunning = false;
    }
}
