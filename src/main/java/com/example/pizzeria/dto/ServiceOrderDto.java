package com.example.pizzeria.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class ServiceOrderDto {
    private Long id;
    private Long cashRegisterId;
    private LocalDateTime createdAt;
    private List<OrderPizzaDto> orderPizza;
    private DinerDto dinner;

    public ServiceOrderDto(Integer id, Integer id1, LocalDateTime createdAt, List<OrderPizzaDto> orderPizzaDtos,
                           DinerDto dinerDto) {

    }
}
