package com.example.pizzeria.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DinerArrivalConfig {
    private DinerArrivalFrequency frequency;
    private int quantity;
}
