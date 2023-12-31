package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.events.PostCookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.events.PreCookingOrderUpdateEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Getter
@Service
public class UniversalCookingManager implements ICookingManager {
    private final ApplicationEventPublisher publisher;
    private final SimpMessagingTemplate messagingTemplate;
    private final PizzeriaConfig config;
    private final CookingInfoFinder cookingInfoFinder;
    private final StageExecutionTimeCalculator stageExecutionTimeCalculator;
    private Map<Order, List<PizzaCookingState>> orders;
    private Map<Cook, PizzaCookingState> cooks;

    public void init() {
        orders = new HashMap<>();
        cooks = new HashMap<>();
        for(int i = 0; i < config.getCooksNumber(); i++) {
            Cook cook = new Cook();
            cooks.put(cook, null);
            cook.start();
        }
    }

    public void terminate() {
        if(cooks == null) {
            return;
        }
        for (Cook cook : cooks.keySet()) {
            cook.interrupt();
        }
        orders.clear();
        cooks.clear();

    }

    @Override
    public synchronized void acceptOrder(Order order) {
        try{
            orders.put(order, order.getOrderedItems().stream().map(orderedItem ->
                    new PizzaCookingState(orderedItem, order.getId())).toList());
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
        for (Cook cook : cooks.keySet()) {
            if (cook.getCookId().equals(cookId)) {
                cook.pauseCook();
                    publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
            }
        }
    }

    @Override
    public synchronized void resumeCook(Integer cookId) {
        for (Cook cook : cooks.keySet()) {
            if (cook.getCookId().equals(cookId)) {
                cook.resumeCook();
                giveNewTaskToCook(cook);
                publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
            }
        }
    }


    private synchronized void giveNewTaskToCook(Cook cook) {
        PizzaCookingState currPizzaStage = cooks.get(cook);
        PizzaCookingState pizzaCookingState = cookingInfoFinder.findFirstNotCompletedPizzaState(orders);
        if(currPizzaStage == null) {
            cooks.put(cook, pizzaCookingState);
        }
        if(pizzaCookingState == null) {
            return;
        }
        pizzaCookingState.modifyLastModifiedAt();
        ICookTask task = createCookTask(cook, pizzaCookingState);
        cook.addTask(task);

        pizzaCookingState.setCookingPizzaStage();
        publisher.publishEvent(new PreCookingOrderUpdateEvent(this, cook, pizzaCookingState));
    }

    private synchronized void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = cookingInfoFinder.findAvailableCook(cooks);
            if(cook == null){
                return;
            }
            cooks.put(cook, pizzaCookingState);
            pizzaCookingState.modifyLastModifiedAt();
            ICookTask task = createCookTask(cook, pizzaCookingState);
            cook.addTask(task);

            pizzaCookingState.setCookingPizzaStage();
            publisher.publishEvent(new PreCookingOrderUpdateEvent(this, cook, pizzaCookingState));
        }
    }

    private synchronized ICookTask createCookTask(Cook cook, PizzaCookingState pizzaCookingState){
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

    private synchronized void handleCallback(PizzaCookingState pizzaCookingState, Cook cook){
        cooks.put(cook, null);
        if(cook.getStatus().equals(CookStatus.PAUSED)) {
            publisher.publishEvent(new PausedCookUpdateEvent(this, cook));
            if(pizzaCookingState.getCompletedAt() == null) {
                handleNewOrderTasks(List.of(pizzaCookingState));
            }
        }
        if(pizzaCookingState.getNextStage().equals(PizzaStage.Completed)) {
            pizzaCookingState.setCurrCookingStage(PizzaStage.Completed);
            pizzaCookingState.setCompletedAt(LocalDateTime.now());
            publisher.publishEvent(new PostCookingOrderUpdateEvent(this, cook, pizzaCookingState));
            if(cook.getStatus().equals(CookStatus.FREE)){
                giveNewTaskToCook(cook);
            }
            checkIsOrderCompleted();
            return;
        }
        publisher.publishEvent(new PostCookingOrderUpdateEvent(this, cook, pizzaCookingState));
        if(cook.getStatus().equals(CookStatus.FREE) &&
                !pizzaCookingState.getCurrCookingStage().equals(PizzaStage.Completed)){
            cooks.put(cook,pizzaCookingState);
            pizzaCookingState.modifyLastModifiedAt();
            cook.addTask(createCookTask(cook, pizzaCookingState));
            pizzaCookingState.setCookingPizzaStage();
            publisher.publishEvent(new PreCookingOrderUpdateEvent(this, cook, pizzaCookingState));
        }

    }
}
