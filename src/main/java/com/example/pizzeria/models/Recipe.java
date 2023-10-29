package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Recipe {
    private String name;
    private List<String> toppings;
    public Recipe() {
        toppings = new ArrayList<>();
    }

    public Recipe(String name, List<String> toppings) {
        this.name = name;
        this.toppings = toppings;
    }

    public void addTopping(String topping) {
        toppings.add(topping);
    }
}
