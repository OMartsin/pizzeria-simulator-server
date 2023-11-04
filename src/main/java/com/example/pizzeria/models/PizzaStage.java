package com.example.pizzeria.models;

public enum PizzaStage {
    Dough, Topping, Baking, Packaging, Completed;

    public PizzaStage getNext() {
        int nextIndex = (this.ordinal() + 1) % PizzaStage.values().length;
        return (nextIndex == 0) ? null : PizzaStage.values()[nextIndex];
    }
}
