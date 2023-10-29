package com.example.pizzeria.PizzeriaConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe {
    private String name;
    private String[] toppings;
    public Recipe() {
        toppings = new String[0];
    }

    public Recipe(String name, String[] toppings) {
        this.name = name;
        this.toppings = toppings;
    }

    public void addTopping(String topping) {
        String[] newToppings = new String[toppings.length + 1];
        for (int i = 0; i < toppings.length; i++) {
            newToppings[i] = toppings[i];
        }
        newToppings[toppings.length] = topping;
        toppings = newToppings;
    }
}
