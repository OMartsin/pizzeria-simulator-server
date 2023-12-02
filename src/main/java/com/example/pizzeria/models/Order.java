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
    private Integer cashRegisterId;
    private List<OrderedItem> orderedItems;
    private Diner diner;
    private LocalDateTime orderTime;

    public Order(Integer cashRegisterId, List<OrderedItem> orderedItems, Diner dinner) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.cashRegisterId = cashRegisterId;
        this.orderedItems = orderedItems;
        this.diner = dinner;
        this.orderTime = LocalDateTime.now();
    }
}
