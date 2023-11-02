package com.example.pizzeria.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Recipe {
    private int id;
    private String name;
    private List<String> toppings;
    private int time;
    private String url;
    public Recipe() {
        toppings = new ArrayList<>();
    }

    public Recipe(int id, String name, List<String> toppings, String url) {
        this.id = id;
        this.name = name;
        this.toppings = toppings;
        this.time = toppings.size() * 2 + 6;
        this.url = url;
    }

    public void addTopping(String topping) {
        toppings.add(topping);
    }
}
