package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.events.PostCookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.events.PreCookingOrderUpdateEvent;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import com.example.pizzeria.models.task.ICookTask;
import com.example.pizzeria.models.task.ITaskCallback;
import com.example.pizzeria.models.task.PizzaHandlingCookTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Getter
@Service
public class SpecializedCookingManager implements ICookingManager {
    private final ApplicationEventPublisher publisher;
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

    public void terminate() {
        if(cooks == null) {
            return;
        }
        for(var cooksMapItem : cookPerStage.entrySet()) {
            for(var cook : cooksMapItem.getValue()) {
                cook.interrupt();
            }
        }
        orders.clear();
        cookPerStage.clear();
        cooks.clear();
    }

    @Override
    public synchronized void acceptOrder(Order order) {
        try {
            orders.put(order, order.getOrderedItems().stream().map(ord ->
                    new PizzaCookingState(ord, order.getId())).toList());
        }
        catch (Exception e){
            System.out.println("Error while accepting order");
            e.printStackTrace();
            return;
        }
        System.out.println("Order " + order.getId() + " accepted");
        System.out.println("Pizzas: ");
        order.getOrderedItems().forEach(orderedItem -> System.out.println(orderedItem.getRecipe().getName()));
        handleNewOrderTasks(orders.get(order));
    }

    @Override
    public synchronized void pauseCook(Integer cookId) {
        for (List<Cook> cooks : cookPerStage.values()) {
            for(Cook cook : cooks) {
                if (cook.getCookId().equals(cookId)) {
                    cook.pauseCook(); //cook status == Paused
                    publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
                    return;
                }
            }
        }
    }

    @Override
    public synchronized void resumeCook(Integer cookId) {
        for (List<Cook> cooks : cookPerStage.values()) {
            for (Cook cook : cooks) {
                if (cook.getCookId().equals(cookId)) {
                    cook.resumeCook();
                    findNewTaskToCook(cook);
                    publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
                    return;
                }
            }
        }
    }

    private synchronized void findNewTaskToCook(Cook cook) {
        PizzaStage pizzaStage = cookingInfoFinder.findPizzaStageByCook(cookPerStage, cook).orElse(null);
        if(pizzaStage == null) {
            return;
        }
        PizzaCookingState pizzaCookingState = cookingInfoFinder.findFirstNotCompletedPizzaState(orders, pizzaStage);
        if(pizzaCookingState == null) {
            return;
        }
        giveCookNewTask(cook, pizzaCookingState);
    }

    private synchronized void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = cookingInfoFinder.findAvailableCook(cookPerStage, pizzaCookingState.getNextStage());
            if(cook == null){
                return;
            }
            giveCookNewTask(cook, pizzaCookingState);
        }
    }

    private void giveCookNewTask(Cook cook, PizzaCookingState pizzaCookingState){
        pizzaCookingState.modifyLastModifiedAt();
        ICookTask task = createCookTask(pizzaCookingState, cook);
        pizzaCookingState.setIsCooking(true);
        cook.addTask(task);
        cooks.put(cook, pizzaCookingState);

        pizzaCookingState.setCookingPizzaStage();
        publisher.publishEvent(new PreCookingOrderUpdateEvent(this, cook, pizzaCookingState));
    }

    private synchronized ICookTask createCookTask(PizzaCookingState pizzaCookingState, Cook cook){
        return new PizzaHandlingCookTask(
                pizzaCookingState, stageExecutionTimeCalculator.getStageExecutionTime(pizzaCookingState.getNextStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        handleCallback(pizzaCookingState, cook);
                    }
                });
    }

    private void checkIsOrderCompleted() {
        var completedOrder = cookingInfoFinder.findCompletedOrder(orders);
        if (completedOrder != null) {
            System.out.println("All pizzas in Order " + completedOrder.getId() + " are completed.");
        }
    }

    private void handleCallback(PizzaCookingState pizzaCookingState, Cook cook) {
        cooks.put(cook, null);
        handlePizzaComplete(pizzaCookingState);
        publisher.publishEvent(new PostCookingOrderUpdateEvent(this, cook, pizzaCookingState));
        if(cook.getStatus().equals(CookStatus.PAUSED)) {
            publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
        }
        if(!pizzaCookingState.getCurrCookingStage().equals(PizzaStage.Completed)){
            handleNewOrderTasks(List.of(pizzaCookingState));
        }
        if(cook.getStatus().equals(CookStatus.FREE)){
            findNewTaskToCook(cook);
        }
    }

    private void handlePizzaComplete(PizzaCookingState pizzaCookingState){
        if(pizzaCookingState.getNextStage().equals(PizzaStage.Completed)) {
            pizzaCookingState.setCurrCookingStage(PizzaStage.Completed);
            pizzaCookingState.setCompletedAt(LocalDateTime.now());
            checkIsOrderCompleted();
        }
    }
}
