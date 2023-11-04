package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Dinner {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private Integer id;
    private String name;
    private Order order;

    public Dinner() {
        id = ID_GENERATOR.getAndIncrement();
    }
    public Dinner(String name, Order order) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.name = name;
        order.setDinnerId(this.id);
        this.order = order;
    }

}