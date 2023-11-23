package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaCookingState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CookingOrderDto {
    private PizzaCookingState pizzaCookingState;
    private Integer toppingId;
    private Integer cookId;
    private Integer orderId;

    public CookingOrderDto(PizzaCookingState pizzaCookingState, Integer toppingId, Integer cookId, Integer orderId) {
        this.toppingId = toppingId;
        this.cookId = cookId;
        this.orderId = orderId;
        this.pizzaCookingState = pizzaCookingState;
    }
}
