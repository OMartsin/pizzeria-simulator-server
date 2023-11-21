package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Order {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private Integer id;
    private List<Recipe> recipes;
    private Diner diner;
    private LocalDateTime createdAt;

    public Order(List<Recipe> recipes, Diner diner) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.recipes = recipes;
        this.diner = diner;
        this.createdAt = LocalDateTime.now();
    }

}
