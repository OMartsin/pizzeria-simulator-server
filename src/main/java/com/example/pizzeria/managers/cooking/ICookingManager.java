package com.example.pizzeria.managers.cooking;

import com.example.pizzeria.models.Order;

public interface ICookingManager {
    void init();
    void acceptOrder(Order order);
    void pauseCook(Integer cookId);
    void resumeCook(Integer cookId);
    void terminate();
}
