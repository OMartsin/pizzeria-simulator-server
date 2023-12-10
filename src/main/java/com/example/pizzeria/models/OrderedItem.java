package com.example.pizzeria.models;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class OrderedItem {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Integer id;
    private Recipe recipe;

    public OrderedItem(Recipe recipe) {
        this.recipe = recipe;
        id = ID_GENERATOR.getAndIncrement();
    }
}
