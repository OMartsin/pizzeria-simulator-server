package com.example.pizzeria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPizzaDto {
    private int id;
    private int orderId;
    private int recipeId;

    public OrderPizzaDto(int id, int orderId, int recipeId) {
        this.id = id;
        this.orderId = orderId;
        this.recipeId = recipeId;
    }
}
