package com.example.pizzeria.models.task;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
public class PizzaHandlingCookTask implements ICookTask {
    private PizzaCookingState pizzaCookingState;
    private Integer executionTime;
    private ITaskCallback callback;

    public void execute(Cook cook) {
        try {
            cook.setBusy();
            pizzaCookingState.setIsCooking(true);
            Thread.sleep(TimeUnit.SECONDS.toMillis(executionTime));
            handlePizza();
            System.out.println(cook.getCookName() + " " + "has finished preparing pizza stage" +
                    pizzaCookingState.getCurrCookingStage() + " for order ID: " + pizzaCookingState.getOrderId() + " in " +
                    pizzaCookingState.getOrderedItem().getRecipe());
            pizzaCookingState.setIsCooking(false);
            cook.setFree();
            pizzaCookingState.setWaitingPizzaStage();
            callback.onTaskCompleted(cook);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handlePizza(){
        pizzaCookingState.modifyLastModifiedAt(); 
        if(pizzaCookingState.getCurrCookingStage() == null) {
            pizzaCookingState.setCurrCookingStage(PizzaStage.Dough);
            return;
        }
        if(pizzaCookingState.getCurrCookingStage().equals(PizzaStage.Topping)) {
            if(pizzaCookingState.getCurrToppingIndex() <
                    pizzaCookingState.getOrderedItem().getRecipe().getToppings().size() - 1) {
                pizzaCookingState.setCurrToppingIndex(pizzaCookingState.getCurrToppingIndex() + 1);
                return;
            }
            if(pizzaCookingState.getCurrToppingIndex() ==
                    pizzaCookingState.getOrderedItem().getRecipe().getToppings().size() - 1) {
                pizzaCookingState.setCurrToppingIndex(null);
            }

        }
        if(pizzaCookingState.getNextStage().equals(PizzaStage.Topping)) {
            pizzaCookingState.setCurrCookingStage(PizzaStage.Topping);
            pizzaCookingState.setCurrToppingIndex(0);
            return;
        }
        var nextPizzaStage = pizzaCookingState.getNextStage();
        pizzaCookingState.setCurrCookingStage(Objects.requireNonNullElse(nextPizzaStage, PizzaStage.Completed));
    }
}
