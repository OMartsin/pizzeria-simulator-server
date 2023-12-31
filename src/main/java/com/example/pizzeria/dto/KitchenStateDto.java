package com.example.pizzeria.dto;

import java.util.List;

public record KitchenStateDto(List<CookDto> cooks, List<OrderDto> orders) {
}
