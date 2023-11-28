package com.example.pizzeria.models.task;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
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
            cook.setStatus(CookStatus.BUSY);
            pizzaCookingState.setIsCooking(true);
            handlePizza();
            Thread.sleep(TimeUnit.SECONDS.toMillis(executionTime));
            System.out.println(cook.getCookName() + " " + "has finished preparing pizza stage" +
                    pizzaCookingState.getCurrStage() + " for order ID: " + pizzaCookingState.getOrderId() + " in " +
                    pizzaCookingState.getRecipe());
            pizzaCookingState.setIsCooking(false);
            if(cook.getStatus().equals(CookStatus.BUSY)) {
                cook.setStatus(CookStatus.FREE);
            }
            callback.onTaskCompleted(cook);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void handlePizza(){
        if(pizzaCookingState.getCurrStage() == PizzaStage.Topping && (pizzaCookingState.getCurrToppingIndex() == null ||
                pizzaCookingState.getCurrToppingIndex() < pizzaCookingState.getRecipe().getToppings().size() - 1)) {
            pizzaCookingState.setCurrToppingIndex
                    (pizzaCookingState.getCurrToppingIndex() == null ? 0 : pizzaCookingState.getCurrToppingIndex() + 1);
            return;
        }
        if(pizzaCookingState.getCurrStage() == PizzaStage.Topping && pizzaCookingState.getCurrToppingIndex() ==
                pizzaCookingState.getRecipe().getToppings().size() - 1) {
            pizzaCookingState.setCurrToppingIndex(null);
        }
        var nextPizzaStage = pizzaCookingState.getNextStage();
        pizzaCookingState.setCurrStage(Objects.requireNonNullElse(nextPizzaStage, PizzaStage.Completed));
        if(pizzaCookingState.getCurrStage() == PizzaStage.Completed) {
            var time = pizzaCookingState.getCompletedAt() != null ? pizzaCookingState.getCompletedAt() : LocalDateTime.now();
            pizzaCookingState.setCompletedAt(time);
        }
    }
}
