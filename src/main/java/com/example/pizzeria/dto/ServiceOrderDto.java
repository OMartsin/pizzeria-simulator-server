package com.example.pizzeria.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderDto(Long id, Long cashRegisterId, LocalDateTime createdAt, List<OrderPizzaDto> orderPizzas, DinerDto diner) {}
