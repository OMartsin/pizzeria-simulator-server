package com.example.pizzeria.PizzeriaConfig;

public enum DinerArrivalFrequency {
    High(1),
    Medium(5),
    Low(10);

    public final int value;

    private DinerArrivalFrequency(int value) {
        this.value = value;
    }
}
