package com.example.pizzeria.managers.cashregister;

import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.models.Dinner;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
public class CashRegister {
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Integer id;
    private final Deque<Dinner> dinners;
    private final ICookingManager cookingManager;

    public CashRegister(ICookingManager cookingManager) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.dinners = new ArrayDeque<>();
        this.cookingManager = cookingManager;
    }

    public void addDinner(Dinner dinner) {
        System.out.println("Before adding dinner to CashRegister: " + dinner.getId());
        this.dinners.add(dinner);
        System.out.println("After adding dinner to CashRegister: " + dinner.getId());
        cookingManager.acceptOrder(dinner.getOrder());
        System.out.println("After processing dinner in CookingManager: " + dinner.getId());
    }

}
