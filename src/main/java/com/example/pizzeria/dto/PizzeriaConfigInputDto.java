package com.example.pizzeria.dto;

import com.example.pizzeria.config.DinerArrivalConfig;
import com.example.pizzeria.models.PizzaStage;

import java.util.List;
import java.util.Map;

public record PizzeriaConfigInputDto(boolean specializedCooksMode, Map<PizzaStage, Integer> cookStageDurationMap,
                                     DinerArrivalConfig dinerArrivalConfig, int cashRegisterQuantity,
                                     List<Integer> menu, Map<PizzaStage, Integer> cooksPerStage) {
}
