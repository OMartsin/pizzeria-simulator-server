package com.example.pizzeria.config;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.Recipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class PizzeriaConfig{
    private boolean specializedCooksMode;
    private Map<PizzaStage, Integer> cookStageDurationMap;
    private DinerArrivalConfig dinerArrivalConfig;
    private Integer cashRegisterQuantity;
    private List<Recipe> menu;
    private Map<PizzaStage, Integer> cooksPerStage;
    private Integer cooksNumber;

    public void updateConfig(PizzeriaConfig config){
        this.specializedCooksMode = config.specializedCooksMode;
        this.cookStageDurationMap = config.cookStageDurationMap;
        this.dinerArrivalConfig = config.dinerArrivalConfig;
        this.cashRegisterQuantity = config.cashRegisterQuantity;
        this.menu = config.menu;
        this.cooksPerStage = config.cooksPerStage;
        this.cooksNumber = config.cooksNumber;
    }
}
