package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.PizzaState;
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
    private Map<Order, List<PizzaState>> orders;
    private Map<Cook, PizzaState> cooks;

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
                    new PizzaState(recipe, order.getId())).toList());
        }
        catch (Exception e){
            System.out.println("Error while accepting order");
            e.printStackTrace();
            return;
        }
        System.out.println("Order " + order.getId() + " accepted");
        System.out.println("Pizzas: ");
        order.getRecipes().forEach(recipe -> System.out.println(recipe.getName()));
        processOrders();
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

    @Override
    public void processOrders() {
        System.out.println("Processing orders");
        while(!orders.isEmpty()){
            Cook cook = findAvailableCook();
            if (cook != null) {
                PizzaState pizzaState = cooks.get(cook);
                if(pizzaState == null) {
                    System.out.println("Finding first not completed pizza");
                    pizzaState = getFirstNotCompletedPizzaState();
                }
                if(pizzaState == null){
                    break;
                }
                if(pizzaState.getCurrStage().equals(PizzaStage.Completed)) {
                    cooks.put(cook, null);
                    break;
                }
                cooks.put(cook, pizzaState);
                ICookTask task = createCookTask(cook, pizzaState);
                cook.addTask(task);
            }
            break;
        }
    }

    private Cook findAvailableCook() {
        for (Cook cook : cooks.keySet()) {
            if (cook.getStatus().equals(CookStatus.FREE))
                return cook;
            }
        return null;
    }

    private Integer getStageExecutionTime(PizzaStage stage){
        return (int) (config.getPizzaStagesTimeCoeffs().get(stage) * config.getMinimumPizzaTime());
    }

    private PizzaState getFirstNotCompletedPizzaState() {
        for (Map.Entry<Order, List<PizzaState>> entry : orders.entrySet()) {
            List<PizzaState> pizzaStates = entry.getValue();

            for (PizzaState pizzaState : pizzaStates) {
                if (pizzaState.getCurrStage() != PizzaStage.Completed &&
                        pizzaState.getIsCooking().equals(false)) {
                    return pizzaState;
                }
            }
        }
        return null;
    }

    private ICookTask createCookTask(Cook cook, PizzaState pizzaState){
        return new PizzaHandlingCookTask(
                cooks.get(cook), getStageExecutionTime(pizzaState.getCurrStage()),
                new ITaskCallback() {
                    @Override
                    public void onTaskCompleted(Cook cook) {
                        if(!pizzaState.getCurrStage().equals(PizzaStage.Completed)) {
                            cook.addTask(createCookTask(cook, pizzaState));
                        }
                        else {
                            cooks.put(cook, null);
                            checkIsOrderCompleted(pizzaState);
                            processOrders();
                        }
                    }
                });
    }

    private void checkIsOrderCompleted(PizzaState pizzaState) {
        Order targetOrder = null;

        for (Map.Entry<Order, List<PizzaState>> entry : orders.entrySet()){
            if(entry.getKey().getId().equals(pizzaState.getOrderId())) {
                targetOrder = entry.getKey();
                break;
            }
        }

        boolean allPizzasCompleted = orders.get(targetOrder).stream()
                .allMatch(pizzaState1 -> pizzaState1.getCurrStage() == PizzaStage.Completed);

        if (allPizzasCompleted) {
            System.out.println("All pizzas in Order " + targetOrder.getId() + " are completed.");
        } else {
            System.out.println("Not all pizzas in Order " + targetOrder.getId() + " are completed yet.");
        }
    }
}
