package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Diner {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();
    private Integer id;
    private String name;
    private Order order;

    public Diner() {
        id = ID_GENERATOR.getAndIncrement();
    }
    public Diner(String name, Order order) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.name = name;
        order.setDiner(this);
        this.order = order;
    }

}
