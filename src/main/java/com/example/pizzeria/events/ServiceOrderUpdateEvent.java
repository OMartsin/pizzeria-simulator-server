package com.example.pizzeria.events;

import com.example.pizzeria.managers.cashregister.CashRegister;
import com.example.pizzeria.models.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ServiceOrderUpdateEvent extends ApplicationEvent implements UpdateEvent {
    private final CashRegister cashRegister;
    private final Order order;

    public ServiceOrderUpdateEvent(Object source, CashRegister cashRegister, Order order) {
        super(source);
        this.cashRegister = cashRegister;
        this.order = order;
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
