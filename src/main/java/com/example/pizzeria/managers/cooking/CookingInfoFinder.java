package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.cook.Cook;
import com.example.pizzeria.models.cook.CookStatus;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CookingInfoFinder {
    public Cook findAvailableCook(Map<PizzaStage, List<Cook>> cooks, PizzaStage pizzaStage) {
        List<Cook> tempCooksList = cooks.get(pizzaStage);
        if(tempCooksList == null || tempCooksList.isEmpty()){
            return null;
        }
        System.out.println(pizzaStage);
        for (Cook cook : tempCooksList) {
            if (cook.getStatus().equals(CookStatus.FREE)) {
                return cook;
            }
        }
        return null;
    }

    public Cook findAvailableCook( Map<Cook, PizzaCookingState> cooks){
        for (Cook cook : cooks.keySet()) {
            if (cook.getStatus().equals(CookStatus.FREE)) {
                return cook;
            }
        }
        return null;
    }

    public PizzaCookingState findFirstNotCompletedPizzaState(Map<Order, List<PizzaCookingState>> orders, PizzaStage pizzaStage) {
        return orders.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getId()))
                .flatMap(entry -> entry.getValue().stream())
                .filter(pizzaCookingState ->
                        pizzaCookingState.getNextStage() == pizzaStage &&
                                !pizzaCookingState.getIsCooking())
                .findFirst()
                .orElse(null);
    }

    public synchronized PizzaCookingState findFirstNotCompletedPizzaState(Map<Order, List<PizzaCookingState>> orders) {
        return orders.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getId()))
                .flatMap(entry -> entry.getValue().stream())
                .filter(pizzaCookingState ->
                        (pizzaCookingState.getCurrCookingStage() == null ||
                                pizzaCookingState.getCompletedAt() == null ) && !pizzaCookingState.getIsCooking())
                .findFirst()
                .orElse(null);
    }

    public Optional<PizzaStage> findPizzaStageByCook( Map<PizzaStage, List<Cook>> cooks, Cook cook) {
        for (Map.Entry<PizzaStage, List<Cook>> entry : cooks.entrySet()) {
            if (entry.getValue().contains(cook)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
    public Order findCompletedOrder( Map<Order,
            List<PizzaCookingState>> orders) {
        for (Map.Entry<Order, List<PizzaCookingState>> entry : orders.entrySet()) {
            List<PizzaCookingState> pizzaCookingStates = entry.getValue();

            if(pizzaCookingStates.stream().allMatch(pizzaCookingState -> pizzaCookingState.getCurrCookingStage() ==
                    PizzaStage.Completed)){
                    return entry.getKey();
            }
        }
        return null;
    }

}
