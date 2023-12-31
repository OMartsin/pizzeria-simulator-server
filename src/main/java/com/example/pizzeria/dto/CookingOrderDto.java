package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaStage;

import java.time.LocalDateTime;

public record CookingOrderDto(PizzaStage currentStage, String currentTopping, Integer cookId, Integer orderId,
                              Integer orderPizzaId, LocalDateTime completedAt, LocalDateTime modifiedAt) {}
