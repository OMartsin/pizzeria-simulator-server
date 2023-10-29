package com.example.pizzeria.config;

public enum DinerArrivalFrequency {
    High(1),
    Medium(5),
    Low(10);

    public final int value;

    DinerArrivalFrequency(int value) {
        this.value = value;
    }
}
