package com.example.pizzeria.config;

public enum DinerArrivalFrequency {
    High(10),
    Medium(25),
    Low(50);

    public final int value;

    DinerArrivalFrequency(int value) {
        this.value = value;
    }
}
