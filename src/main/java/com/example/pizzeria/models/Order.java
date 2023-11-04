package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Order {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private Integer id;
    private List<Recipe> recipes;
    private Integer dinnerId;

    public Order(List<Recipe> recipes, Integer dinnerId) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.recipes = recipes;
        this.dinnerId = dinnerId;
    }

}
