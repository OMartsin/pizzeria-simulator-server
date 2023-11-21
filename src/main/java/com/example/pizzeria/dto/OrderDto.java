package com.example.pizzeria.dto;

import com.example.pizzeria.models.Diner;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Integer id, Integer cashRegisterId, Diner diner, List<PizzaCookingStateDto> orderPizzas,
                       LocalDateTime orderTime) {
}
