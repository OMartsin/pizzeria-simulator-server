package com.example.pizzeria.PizzeriaConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DinerArrivalConfig {
    private DinerArrivalFrequency frequency;
    private int quantity;
}
