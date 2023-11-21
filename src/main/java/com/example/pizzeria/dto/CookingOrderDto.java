package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaCookingState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CookingOrderDto {
    private PizzaCookingState pizzaCookingState;
    private int toppingId;
    private int cookId;
    private int orderId;

    public CookingOrderDto(PizzaCookingState pizzaCookingState, int toppingId, int cookId, int orderId) {
        this.toppingId = toppingId;
        this.cookId = cookId;
        this.orderId = orderId;
        this.pizzaCookingState = pizzaCookingState;
    }
}
