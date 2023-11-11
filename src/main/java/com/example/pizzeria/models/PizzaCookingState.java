package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PizzaCookingState {
    private final Recipe recipe;
    private final Integer orderId;
    private PizzaStage currStage;
    private Integer currToppingIndex;

    private Boolean isCooking = false;

    public PizzaCookingState(Recipe recipe, Integer orderId) {
        this.recipe = recipe;
        this.orderId = orderId;
        this.currStage = PizzaStage.Dough;
        this.currToppingIndex = 0;
    }

}