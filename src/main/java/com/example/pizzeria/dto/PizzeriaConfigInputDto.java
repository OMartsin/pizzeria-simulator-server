package com.example.pizzeria.dto;

import com.example.pizzeria.config.DinerArrivalConfig;
import com.example.pizzeria.models.PizzaStage;

import java.util.List;
import java.util.Map;

public record PizzeriaConfigInputDto(boolean specializedCooksMode,
                                     Integer minimumPizzaTime, DinerArrivalConfig dinerArrivalConfig,
                                     Integer cashRegisterQuantity, List<Integer> menu,
                                     Map<PizzaStage, Integer> cooksPerStage, Integer cooksNumber) {
}
