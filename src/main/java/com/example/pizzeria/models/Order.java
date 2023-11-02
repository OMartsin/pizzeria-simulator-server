package com.example.pizzeria.models;

import java.util.List;

public record Order(List<Recipe> pizzas) {
}