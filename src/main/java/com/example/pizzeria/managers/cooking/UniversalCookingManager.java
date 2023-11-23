package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.events.CookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import com.example.pizzeria.models.task.ICookTask;
import com.example.pizzeria.models.task.ITaskCallback;
import com.example.pizzeria.models.task.PizzaHandlingCookTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Getter
@Service
public class UniversalCookingManager implements ICookingManager {
    @Autowired
    ApplicationEventPublisher publisher;

    private final PizzeriaConfig config;
    private Map<Order, List<PizzaCookingState>> orders;
    private Map<Cook, PizzaCookingState> cooks;
    private final CookingInfoFinder cookingInfoFinder;
    private final StageExecutionTimeCalculator stageExecutionTimeCalculator;

    public void init() {
        orders = new HashMap<>();
        cooks = new HashMap<>();
        for(int i = 0; i < config.getCooksNumber(); i++) {
            Cook cook = new Cook();
            cooks.put(cook, null);
            cook.start();
        }
    }

    @Override
    public void acceptOrder(Order order) {
        try{
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
        for (Cook cook : cooks.keySet()) {
            if (cook.getCookId().equals(cookId)) {
                cook.pauseCook();
                publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
            }
        }
    }

    @Override
    public void resumeCook(Integer cookId) {
        for (Cook cook : cooks.keySet()) {
            if (cook.getCookId().equals(cookId)) {
                cook.resumeCook();
                giveNewTaskToCook(cook);
                publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
            }
        }
    }


    private void giveNewTaskToCook(Cook cook) {
        PizzaCookingState currPizzaStage = cooks.get(cook);
        PizzaCookingState pizzaCookingState = cookingInfoFinder.findFirstNotCompletedPizzaState(orders);
        if(currPizzaStage == null) {
            cooks.put(cook, pizzaCookingState);
        }
        if(pizzaCookingState == null) {
            return;
        }
        ICookTask task = createCookTask(cook, pizzaCookingState);
        cook.addTask(task);

        publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
    }

    private void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = cookingInfoFinder.findAvailableCook(cooks);
            if(cook == null){
                return;
            }
            cooks.put(cook, pizzaCookingState);
            ICookTask task = createCookTask(cook, pizzaCookingState);
            cook.addTask(task);

            publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
        }
    }

    private ICookTask createCookTask(Cook cook, PizzaCookingState pizzaCookingState){
        return new PizzaHandlingCookTask(
                cooks.get(cook), stageExecutionTimeCalculator.getStageExecutionTime(pizzaCookingState.getNextStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        if(!pizzaCookingState.getNextStage().equals(PizzaStage.Completed)) {
                            if(cook.getStatus().equals(CookStatus.FREE)){
                                cook.addTask(createCookTask(cook, pizzaCookingState));
                            }
                        }
                        else {
                            pizzaCookingState.setCompletedAt(LocalDateTime.now());
                            pizzaCookingState.setCurrStage(PizzaStage.Completed);
                            cooks.put(cook, null);
                            checkIsOrderCompleted();
                            if(cook.getStatus().equals(CookStatus.FREE)){
                                giveNewTaskToCook(cook);
                            }
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
