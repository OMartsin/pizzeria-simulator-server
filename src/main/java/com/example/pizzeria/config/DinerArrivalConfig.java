package com.example.pizzeria.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DinerArrivalConfig {
    private DinerArrivalFrequency frequency;
    private int quantity;
}
