package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class PizzaCookingState {
    private final OrderedItem orderedItem;
    private final Integer orderId;
    private PizzaStage currStage;
    private Integer currToppingIndex;
    private LocalDateTime completedAt;

    private Boolean isCooking = false;

    public PizzaCookingState(OrderedItem orderedItem, Integer orderId) {
        this.orderedItem = orderedItem;
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

    public String getCurrentTopping() {
        if(currToppingIndex == null)
            return null;
        return orderedItem.getRecipe().getToppings().get(currToppingIndex);
    }
}
