package com.example.pizzeria.models.task;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaState;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class PizzaHandlingCookTask implements ICookTask {
    private PizzaState pizzaState;
    private Integer executionTime;
    private ITaskCallback callback;

    public void execute(Cook cook) {
        try {
            cook.setStatus(CookStatus.BUSY);
            pizzaState.setIsCooking(true);
            System.out.println(cook.getCookName() + " " + "has finished preparing pizza stage" +
                    pizzaState.getCurrStage() + " for order ID: " + pizzaState.getOrderId() + " in " +
                    pizzaState.getRecipe());

            handlePizza();
            Thread.sleep(executionTime);


            cook.setStatus(CookStatus.FREE);
            pizzaState.setIsCooking(false);
            callback.onTaskCompleted(cook);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void handlePizza(){
        if(pizzaState.getCurrStage() == PizzaStage.Topping && (pizzaState.getCurrToppingIndex() == null ||
                pizzaState.getCurrToppingIndex() < pizzaState.getRecipe().getToppings().size())) {
            pizzaState.setCurrToppingIndex
                    (pizzaState.getCurrToppingIndex() == null ? 0 : pizzaState.getCurrToppingIndex() + 1);
            return;
        }
        var nextPizzaStage = pizzaState.getCurrStage().getNext();
        pizzaState.setCurrStage(Objects.requireNonNullElse(nextPizzaStage, PizzaStage.Completed));
    }
}
