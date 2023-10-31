package com.example.pizzeria;

import com.example.pizzeria.config.PizzeriaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final PizzeriaConfig config;
    private final DinersGenerator dinersGenerator;
    public void init() {
        dinersGenerator.setDinerArrivalConfig(config.getDinerArrivalConfig());
    }
}
