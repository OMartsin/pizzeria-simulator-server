package com.example.pizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ServiceOrderDto {
    private Long id;
    private Long cashRegisterId;
    private LocalDateTime createdAt;
    private List<OrderPizzaDto> orderPizza;
    private DinerDto dinner;

}
