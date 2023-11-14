package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.events.CookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import com.example.pizzeria.models.task.ICookTask;
import com.example.pizzeria.models.task.ITaskCallback;
import com.example.pizzeria.models.task.PizzaHandlingCookTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SpecializedCookingManager implements ICookingManager {
    @Autowired
    ApplicationEventPublisher publisher;

    private final PizzeriaConfig config;
    private Map<Order, List<PizzaCookingState>> orders;
    private Map<PizzaStage, List<Cook>> cooks;

    public void init() {
        orders = new HashMap<>();
        cooks = new HashMap<>();
        for(var cooksMapItem : config.getCooksPerStage().entrySet()) {
            List<Cook> tempCooks = new ArrayList<>();
            for(int i = 0; i < cooksMapItem.getValue(); i++) {
                var cook = new Cook();
                tempCooks.add(cook);
                cook.start();
            }
            cooks.put(cooksMapItem.getKey(), tempCooks);
            System.out.println("Cooking stage: " + cooksMapItem.getKey() + " cooks: ");
            tempCooks.forEach(cook -> System.out.println(cook.getCookName()));
        }
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
        pauseCook(1);
        order.getRecipes().forEach(recipe -> System.out.println(recipe.getName()));
        handleNewOrderTasks(orders.get(order));
    }

    @Override
    public void pauseCook(Integer cookId) {
        for (List<Cook> cooks : cooks.values()) {
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
        for (List<Cook> cooks : cooks.values()) {
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
        PizzaStage pizzaStage = findPizzaStageByCook(cook).orElse(null);
        if(pizzaStage == null) {
            return;
        }
        PizzaCookingState pizzaCookingState = getFirstNotCompletedPizzaStateWithStage(pizzaStage);
        if(pizzaCookingState == null) {
            return;
        }
        ICookTask task = createCookTask(pizzaCookingState);
        pizzaCookingState.setIsCooking(true);
        cook.addTask(task);

        publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
    }

    private void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = findAvailableCook(pizzaCookingState.getCurrStage());
            if(cook == null){
                return;
            }
            ICookTask task = createCookTask(pizzaCookingState);
            pizzaCookingState.setIsCooking(true);
            cook.addTask(task);

            publisher.publishEvent(new CookingOrderUpdateEvent(this, cook, pizzaCookingState));
        }
    }



    private Cook findAvailableCook(PizzaStage pizzaStage) {
        List<Cook> tempCooksList = cooks.get(pizzaStage);
        for (Cook cook : tempCooksList) {
            if (cook.getStatus().equals(CookStatus.FREE)) {
                return cook;
            }
        }
        return null;
    }

    public Optional<PizzaStage> findPizzaStageByCook(Cook cook) {
        for (Map.Entry<PizzaStage, List<Cook>> entry : cooks.entrySet()) {
            if (entry.getValue().contains(cook)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    private Integer getStageExecutionTime(PizzaStage stage){
        return (int) (config.getPizzaStagesTimeCoeffs().get(stage) * config.getMinimumPizzaTime());
    }

    private PizzaCookingState getFirstNotCompletedPizzaStateWithStage(PizzaStage stage) {
        for (Map.Entry<Order, List<PizzaCookingState>> entry : orders.entrySet()) {
            List<PizzaCookingState> pizzaCookingStates = entry.getValue();

            for (PizzaCookingState pizzaCookingState : pizzaCookingStates) {
                if (pizzaCookingState.getCurrStage() == stage &&
                        pizzaCookingState.getIsCooking().equals(false)) {
                    return pizzaCookingState;
                }
            }
        }
        return null;
    }

    private ICookTask createCookTask(PizzaCookingState pizzaCookingState){
        return new PizzaHandlingCookTask(
                pizzaCookingState, getStageExecutionTime(pizzaCookingState.getCurrStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        giveNewTaskToCook(cook);
                        if(pizzaCookingState.getCurrStage() != PizzaStage.Completed) {
                            handleNewOrderTasks(List.of(pizzaCookingState));
                        }
                        else{
                            checkIsOrderCompleted(pizzaCookingState);
                        }
                    }
                });
    }

    private void checkIsOrderCompleted(PizzaCookingState pizzaCookingState) {
        Order targetOrder = null;

        for (Map.Entry<Order, List<PizzaCookingState>> entry : orders.entrySet()){
            if(entry.getKey().getId().equals(pizzaCookingState.getOrderId())) {
                targetOrder = entry.getKey();
                break;
            }
        }

        boolean allPizzasCompleted = orders.get(targetOrder).stream()
                .allMatch(pizzaCookingState1 -> pizzaCookingState1.getCurrStage() == PizzaStage.Completed);

        if (allPizzasCompleted) {
            System.out.println("All pizzas in Order " + targetOrder.getId() + " are completed.");
        } else {
            System.out.println("Not all pizzas in Order " + targetOrder.getId() + " are completed yet.");
        }
    }
}
