package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import com.example.pizzeria.models.task.ICookTask;
import com.example.pizzeria.models.task.ITaskCallback;
import com.example.pizzeria.models.task.PizzaHandlingCookTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UniversalCookingManager implements ICookingManager {
    private final PizzeriaConfig config;
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
            }
        }
    }

    @Override
    public void resumeCook(Integer cookId) {
        for (Cook cook : cooks.keySet()) {
            if (cook.getCookId().equals(cookId)) {
                cook.resumeCook();
            }
        }
    }


    private void giveNewTaskToCook(Cook cook) {
        PizzaCookingState currPizzaStage = cooks.get(cook);
        PizzaCookingState pizzaCookingState = getFirstNotCompletedPizzaState();
        if(currPizzaStage == null) {
            cooks.put(cook, pizzaCookingState);
        }
        if(pizzaCookingState == null) {
            return;
        }
        ICookTask task = createCookTask(cook, pizzaCookingState);
        cook.addTask(task);
    }

    private void handleNewOrderTasks(List<PizzaCookingState> pizzaCookingStates){
        for (PizzaCookingState pizzaCookingState : pizzaCookingStates.stream().filter(
                pizzaCookingState1 -> pizzaCookingState1.getIsCooking().equals(false)).toList()) {
            Cook cook = findAvailableCook();
            if(cook == null){
                return;
            }
            cooks.put(cook, pizzaCookingState);
            ICookTask task = createCookTask(cook, pizzaCookingState);
            cook.addTask(task);
        }
    }

    private Cook findAvailableCook() {
        for (Cook cook : cooks.keySet()) {
            if (cook.getStatus().equals(CookStatus.FREE) && cooks.get(cook) == null)
                return cook;
            }
        return null;
    }

    private Integer getStageExecutionTime(PizzaStage stage){
        return (int) (config.getPizzaStagesTimeCoeffs().get(stage) * config.getMinimumPizzaTime());
    }

    private PizzaCookingState getFirstNotCompletedPizzaState() {
        for (Map.Entry<Order, List<PizzaCookingState>> entry : orders.entrySet()) {
            List<PizzaCookingState> pizzaCookingStates = entry.getValue();

            for (PizzaCookingState pizzaCookingState : pizzaCookingStates) {
                if (pizzaCookingState.getCurrStage() != PizzaStage.Completed &&
                        pizzaCookingState.getIsCooking().equals(false)) {
                    return pizzaCookingState;
                }
            }
        }
        return null;
    }

    private ICookTask createCookTask(Cook cook, PizzaCookingState pizzaCookingState){
        return new PizzaHandlingCookTask(
                cooks.get(cook), getStageExecutionTime(pizzaCookingState.getCurrStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        if(!pizzaCookingState.getCurrStage().equals(PizzaStage.Completed)) {
                            cook.addTask(createCookTask(cook, pizzaCookingState));
                        }
                        else {
                            cooks.put(cook, null);
                            checkIsOrderCompleted(pizzaCookingState);
                            giveNewTaskToCook(cook);
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
