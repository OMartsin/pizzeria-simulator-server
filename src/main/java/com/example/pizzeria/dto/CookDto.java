package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.cook.CookStatus;

public record CookDto(Integer id, String name, PizzaStage specialization, CookStatus status,
                      Integer orderId, Integer orderPizzaId) {
}
