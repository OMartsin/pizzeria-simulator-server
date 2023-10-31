package com.example.pizzeria;

import com.example.pizzeria.config.DinerArrivalConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class DinersGenerator {
    private DinerArrivalConfig dinerArrivalConfig;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> taskHandle;

    public void setDinerArrivalConfig(DinerArrivalConfig dinerArrivalConfig) {
        this.dinerArrivalConfig = dinerArrivalConfig;
    }

    public void start() throws IllegalStateException {
        checkConfigNotNull();

        Runnable generateDiners = () -> System.out.println("New Diner");
        taskHandle = scheduler.scheduleAtFixedRate(generateDiners,
                0,
                dinerArrivalConfig.getFrequency().value,
                TimeUnit.SECONDS);

    }

    public void pause() throws IllegalStateException {
        checkConfigNotNull();

        taskHandle.cancel(false);
    }

    private void checkConfigNotNull() throws IllegalStateException {
        if (dinerArrivalConfig == null) {
            throw new IllegalStateException("DinerArrivalConfig is not initialized");
        }
    }

}