package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.PizzaStage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageExecutionTimeCalculator {
    private final PizzeriaConfig config;

    public int getStageExecutionTime(PizzaStage pizzaStage){
        return (int) (config.getPizzaStagesTimeCoeffs().get(pizzaStage) * config.getMinimumPizzaTime());
    }
}
