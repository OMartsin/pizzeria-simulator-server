package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class PizzaCookingState {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Integer id;
    private final Recipe recipe;
    private final Integer orderId;
    private PizzaStage currStage;
    private Integer currToppingIndex;
    private LocalDateTime completedAt;

    private Boolean isCooking = false;

    public PizzaCookingState(Recipe recipe, Integer orderId) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.recipe = recipe;
        this.orderId = orderId;
        this.currStage = null;
        this.currToppingIndex = null;
    }

    public PizzaStage getNextStage() {
        if(currStage == null) {
            return PizzaStage.Dough;
        }
        return this.currStage.getNext();
    }
}
