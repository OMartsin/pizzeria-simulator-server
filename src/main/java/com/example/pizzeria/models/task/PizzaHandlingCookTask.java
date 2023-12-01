package com.example.pizzeria.models.task;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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
                    pizzaCookingState.getCurrCookingStage() + " for order ID: " + pizzaCookingState.getOrderId() + " in " +
                    pizzaCookingState.getOrderedItem().getRecipe());
          
            pizzaCookingState.setIsCooking(false);
            cook.setFree();
            pizzaCookingState.setWaitingPizzaStage();
            callback.onTaskCompleted(cook);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

        if(pizzaCookingState.getNextStage().equals(PizzaStage.Topping)) {
            pizzaCookingState.setCurrCookingStage(PizzaStage.Topping);
            pizzaCookingState.setCurrToppingIndex(0);
            return;
        }
        if(pizzaCookingState.getCurrCookingStage() == PizzaStage.Topping) {
            if(pizzaCookingState.getCurrToppingIndex() <
                    pizzaCookingState.getOrderedItem().getRecipe().getToppings().size() - 1){
                pizzaCookingState.setCurrToppingIndex(pizzaCookingState.getCurrToppingIndex() + 1);
            }
            else {
                pizzaCookingState.setCurrToppingIndex(null);
            }
        }
        var nextPizzaStage = pizzaCookingState.getNextStage();
        pizzaCookingState.setCurrCookingStage(Objects.requireNonNullElse(nextPizzaStage, PizzaStage.Completed));
        if(pizzaCookingState.getCurrCookingStage() == PizzaStage.Completed) {
            var time = pizzaCookingState.getCompletedAt() != null ? pizzaCookingState.getCompletedAt() : LocalDateTime.now();
            pizzaCookingState.setCompletedAt(time);
        }
    }
}
