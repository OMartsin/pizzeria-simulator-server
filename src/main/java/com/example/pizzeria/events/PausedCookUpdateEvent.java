package com.example.pizzeria.events;

import com.example.pizzeria.models.cook.Cook;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class PausedCookUpdateEvent extends ApplicationEvent implements UpdateEvent {
    private final Cook cook;

    public PausedCookUpdateEvent(Object source, Cook cook) {
        super(source);
        this.cook = cook;
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
