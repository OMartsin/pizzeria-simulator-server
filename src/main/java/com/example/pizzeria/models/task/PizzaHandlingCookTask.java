package com.example.pizzeria.models.task;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class PizzaHandlingCookTask implements ICookTask {
    private PizzaCookingState pizzaCookingState;
    private Integer executionTime;
    private ITaskCallback callback;

    public void execute(Cook cook) {
        try {
            cook.setStatus(CookStatus.BUSY);
            pizzaCookingState.setIsCooking(true);

            Thread.sleep(executionTime);
            System.out.println(cook.getCookName() + " " + "has finished preparing pizza stage" +
                    pizzaCookingState.getCurrStage() + " for order ID: " + pizzaCookingState.getOrderId() + " in " +
                    pizzaCookingState.getRecipe());
            handlePizza();
            pizzaCookingState.setIsCooking(false);
            cook.setStatus(CookStatus.FREE);
            callback.onTaskCompleted(cook);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void handlePizza(){
        if(pizzaCookingState.getCurrStage() == PizzaStage.Topping && (pizzaCookingState.getCurrToppingIndex() == null ||
                pizzaCookingState.getCurrToppingIndex() < pizzaCookingState.getRecipe().getToppings().size())) {
            pizzaCookingState.setCurrToppingIndex
                    (pizzaCookingState.getCurrToppingIndex() == null ? 0 : pizzaCookingState.getCurrToppingIndex() + 1);
            return;
        }
        var nextPizzaStage = pizzaCookingState.getCurrStage().getNext();
        pizzaCookingState.setCurrStage(Objects.requireNonNullElse(nextPizzaStage, PizzaStage.Completed));
    }
}
