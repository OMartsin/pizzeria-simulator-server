package com.example.pizzeria.models;

public enum PizzaStage {
    Dough, Topping, Baking, Packaging, Completed, Waiting;

    public PizzaStage getNext() {
        if(this == Dough) {
            return Topping;
        }
        if(this == Topping) {
            return Baking;
        }
        if(this == Baking) {
            return Packaging;
        }
        if(this == Packaging) {
            return Completed;
        }
        return null;
    }
}
