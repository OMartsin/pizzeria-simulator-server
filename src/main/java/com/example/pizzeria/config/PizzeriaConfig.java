package com.example.pizzeria.config;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.Recipe;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class PizzeriaConfig {
    private boolean specializedCooksMode;
    private Map<PizzaStage, Double> pizzaStagesTimeCoeffs;
    private Integer minimumPizzaTime;
    private DinerArrivalConfig dinerArrivalConfig;
    private Integer cashRegisterQuantity;
    private List<Recipe> menu;
    private Map<PizzaStage, Integer> cooksPerStage;
    private Integer cooksNumber;

    public void updateConfig(PizzeriaConfig config){
        this.specializedCooksMode = config.specializedCooksMode;
        this.pizzaStagesTimeCoeffs = config.pizzaStagesTimeCoeffs;
        this.minimumPizzaTime = config.minimumPizzaTime;
        this.dinerArrivalConfig = config.dinerArrivalConfig;
        this.cashRegisterQuantity = config.cashRegisterQuantity;
        this.menu = config.menu;
        this.cooksPerStage = config.cooksPerStage;
        this.cooksNumber = config.cooksNumber;
    }

    public void updateConfig(boolean specializedCooksMode,
                             Map<PizzaStage, Double> pizzaStagesTimeCoeffs,
                             Integer minimumPizzaTime, DinerArrivalConfig dinerArrivalConfig,
                             Integer cashRegisterQuantity, List<Recipe> menu,
                             Map<PizzaStage, Integer> cooksPerStage, Integer cooksNumber) {
        this.specializedCooksMode = specializedCooksMode;
        this.pizzaStagesTimeCoeffs = pizzaStagesTimeCoeffs;
        this.minimumPizzaTime = minimumPizzaTime;
        this.dinerArrivalConfig = dinerArrivalConfig;
        this.cashRegisterQuantity = cashRegisterQuantity;
        this.menu = menu;
        this.cooksPerStage = cooksPerStage;
        this.cooksNumber = cooksNumber;
    }

}
