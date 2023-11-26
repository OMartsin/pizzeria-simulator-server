package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CookingOrderDto {
    private PizzaStage currentStage;
    private Integer toppingId;
    private Integer cookId;
    private Integer orderId;
}
