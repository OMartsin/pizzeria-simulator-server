package com.example.pizzeria.dto;

import com.example.pizzeria.models.PizzaStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CookingOrderDto {
    private PizzaStage currentStage;
    private String currentTopping;
    private Integer cookId;
    private Integer orderId;
    private Integer orderPizzaId;
    private LocalDateTime completedAt;
}
