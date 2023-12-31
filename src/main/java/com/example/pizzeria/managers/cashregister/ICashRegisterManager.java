package com.example.pizzeria.managers.cashregister;

import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.models.Diner;

public interface ICashRegisterManager {
    void acceptDinner(Diner diner);
    void init(int cashRegistersNumber, ICookingManager cookingManager);
    void terminate();
}
