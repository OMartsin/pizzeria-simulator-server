package com.example.pizzeria.events;

import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import org.springframework.context.ApplicationEvent;

public class CookingOrderUpdateEvent extends ApplicationEvent implements UpdateEvent {
    private final Cook cook;
    private final PizzaCookingState pizzaCookingState;

    public CookingOrderUpdateEvent(Object source, Cook cook, PizzaCookingState pizzaCookingState) {
        super(source);
        this.cook = cook;
        this.pizzaCookingState = pizzaCookingState;
    }

    @Override
    public String toString() {
        return null;
    }
    @Override
    public String toJson() {
        return null;
    }
}