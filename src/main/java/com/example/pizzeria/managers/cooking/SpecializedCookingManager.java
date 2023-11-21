package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.events.CookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.task.ICookTask;
import com.example.pizzeria.models.task.ITaskCallback;
import com.example.pizzeria.models.task.PizzaHandlingCookTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Getter
@Service
public class SpecializedCookingManager implements ICookingManager {
    ApplicationEventPublisher publisher;

    private final PizzeriaConfig config;
    private Map<Order, List<PizzaCookingState>> orders;
    private Map<PizzaStage, List<Cook>> cookPerStage;
    private Map<Cook, PizzaCookingState> cooks;
    private final CookingInfoFinder cookingInfoFinder;
    private final StageExecutionTimeCalculator stageExecutionTimeCalculator;

    public void init() {
        orders = new HashMap<>();
        cookPerStage = new HashMap<>();
        for(var cooksMapItem : config.getCooksPerStage().entrySet()) {
            List<Cook> tempCooks = new ArrayList<>();
            for(int i = 0; i < cooksMapItem.getValue(); i++) {
                var cook = new Cook();
                tempCooks.add(cook);
                cook.start();
            }
            cookPerStage.put(cooksMapItem.getKey(), tempCooks);
            System.out.println("Cooking stage: " + cooksMapItem.getKey() + " cooks: ");
            tempCooks.forEach(cook -> System.out.println(cook.getCookName()));
        }
        cooks = new HashMap<>(){
            {
                for(var cooksMapItem : cookPerStage.entrySet()) {
                    for(var cook : cooksMapItem.getValue()) {
                        put(cook, null);
                    }
                }
            }
        };
    }

    @Override
    public void acceptOrder(Order order) {
        try {
            orders.put(order, order.getRecipes().stream().map(recipe ->
                    new PizzaCookingState(recipe, order.getId())).toList());
        }
        catch (Exception e){
            System.out.println("Error while accepting order");
            e.printStackTrace();
            return;
        }
        System.out.println("Order " + order.getId() + " accepted");
        System.out.println("Pizzas: ");
        order.getRecipes().forEach(recipe -> System.out.println(recipe.getName()));
        handleNewOrderTasks(orders.get(order));
    }

    @Override
    public void pauseCook(Integer cookId) {
        for (List<Cook> cooks : cookPerStage.values()) {
            for(Cook cook : cooks) {
                if (cook.getCookId().equals(cookId)) {
                    cook.pauseCook();
                    publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
                    return;
                }
            }
        }
    }

    @Override
    public void resumeCook(Integer cookId) {
        for (List<Cook> cooks : cookPerStage.values()) {
            for (Cook cook : cooks) {
                if (cook.getCookId().equals(cookId)) {
                    cook.resumeCook();
                    publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
                    return;
                }
            }
        }
    }

    private void giveNewTaskToCook(Cook cook) {
        PizzaStage pizzaStage = cookingInfoFinder.findPizzaStageByCook(cookPerStage, cook).orElse(null);
        if(pizzaStage == null) {
            return;
        }
        PizzaCookingState pizzaCookingState = cookingInfoFinder.findFirstNotCompletedPizzaState(orders, pizzaStage);
        if(pizzaCookingState == null) {
            return;
        }
        ICookTask task = createCookTask(pizzaCookingState);
        pizzaCookingState.setIsCooking(true);
        cook.addTask(task);
        cooks.put(cook, pizzaCookingState);

        publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
    }

    private void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = cookingInfoFinder.findAvailableCook(cookPerStage, pizzaCookingState.getNextStage());
            if(cook == null){
                return;
            }
            ICookTask task = createCookTask(pizzaCookingState);
            pizzaCookingState.setIsCooking(true);
            cook.addTask(task);
            cooks.put(cook, pizzaCookingState);

            publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
        }
    }

    private ICookTask createCookTask(PizzaCookingState pizzaCookingState){
        return new PizzaHandlingCookTask(
                pizzaCookingState, stageExecutionTimeCalculator.getStageExecutionTime(pizzaCookingState.getNextStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        giveNewTaskToCook(cook);
                        if(pizzaCookingState.getCurrStage() != PizzaStage.Completed) {
                            handleNewOrderTasks(List.of(pizzaCookingState));
                        }
                        else{
                            checkIsOrderCompleted();
                        }
                    }
                });
    }

    private void checkIsOrderCompleted() {
        var completedOrder = cookingInfoFinder.findCompletedOrder(orders);
        if (completedOrder != null) {
            System.out.println("All pizzas in Order " + completedOrder.getId() + " are completed.");
        }
    }
}
