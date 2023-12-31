package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaStage;

import java.time.LocalDateTime;

public record PizzaCookingStateDto(Integer id, Integer orderId, Integer recipeId, PizzaStage currentStage,
                                   String currentTopping, LocalDateTime completedAt, LocalDateTime modifiedAt) {
}
