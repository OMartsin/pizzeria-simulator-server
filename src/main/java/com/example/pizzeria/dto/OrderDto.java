package com.example.pizzeria.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Integer id, Integer cashRegisterId, DinerDto diner, List<PizzaCookingStateDto> orderPizzas,
                       LocalDateTime createdAt) {
}
