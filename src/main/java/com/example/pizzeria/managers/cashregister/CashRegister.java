package com.example.pizzeria.managers.cashregister;

import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.models.Diner;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
public class CashRegister {
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Integer id;
    private final Deque<Diner> diners;
    private final ICookingManager cookingManager;

    public CashRegister(@Qualifier("specializedCookingManager") ICookingManager cookingManager) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.diners = new ArrayDeque<>();
        this.cookingManager = cookingManager;
    }

    public void addDinner(Diner diner) {
        diner.getOrder().setCashRegisterId(this.id);
        System.out.println("Before adding dinner to CashRegister: " + diner.getId());
        this.diners.add(diner);
        System.out.println("After adding dinner to CashRegister: " + diner.getId());
        cookingManager.acceptOrder(diner.getOrder());
        System.out.println("After processing dinner in CookingManager: " + diner.getId());
    }

}
