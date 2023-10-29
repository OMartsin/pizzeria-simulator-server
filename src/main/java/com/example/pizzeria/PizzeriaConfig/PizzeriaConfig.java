package com.example.pizzeria.PizzeriaConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class PizzeriaConfig{
    private boolean specializedCooksMode;
    private Map<PizzaStage, Integer> cookStageDurationMap;
    private DinerArrivalConfig dinerArrivalConfig;
    private int cashRegisterQuantity;
    private Recipe[] menu;
    private Map<PizzaStage, Integer> cooksPerStage;



}
