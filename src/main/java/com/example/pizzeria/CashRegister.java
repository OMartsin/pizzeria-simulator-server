package com.example.pizzeria;

import com.example.pizzeria.models.Dinner;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
public class CashRegister {
    private Deque<Dinner> dinners;

    public CashRegister() {
        this.dinners = new ArrayDeque<>();
    }

    public void addDinner(Dinner dinner) {
        this.dinners.add(dinner);
    }

}
