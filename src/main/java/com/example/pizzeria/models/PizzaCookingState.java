package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PizzaCookingState {
    private final OrderedItem orderedItem;
    private final Integer orderId;
    private PizzaStage currCookingStage;
    private PizzaStage currPizzaStage;
    private Integer currToppingIndex;
    private LocalDateTime completedAt;
    private Boolean isCooking = false;

    public PizzaCookingState(OrderedItem orderedItem, Integer orderId) {
        this.orderedItem = orderedItem;
        this.orderId = orderId;
        this.currCookingStage = null;
        this.currPizzaStage = PizzaStage.Waiting;
        this.currToppingIndex = null;
    }

    public PizzaStage getNextStage() {
        if(currCookingStage == null) {
            return PizzaStage.Dough;
        }
        return this.currCookingStage.getNext();
    }

    public String getCurrentTopping() {
        if(currToppingIndex == null)
            return null;
        return orderedItem.getRecipe().getToppings().get(currToppingIndex);
    }

    //BUTT PLUG
    public String getNextTopping() {
        if(currToppingIndex == null)
            return orderedItem.getRecipe().getToppings().get(0);
        if(currToppingIndex + 1 >= orderedItem.getRecipe().getToppings().size())
            return null;
        return orderedItem.getRecipe().getToppings().get(currToppingIndex + 1);
    }

    public void setCookingPizzaStage() {
        this.currPizzaStage = getNextStage();
    }

    public void setWaitingPizzaStage() {
        if(getNextStage().equals(PizzaStage.Completed)){
            this.currPizzaStage = PizzaStage.Completed;
            return;
        }
        this.currPizzaStage = PizzaStage.Waiting;
    }
}
