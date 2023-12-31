package com.example.pizzeria.models.task;

import com.example.pizzeria.models.cook.Cook;

public interface ITaskCallback {
    void onTaskCompleted(Cook cook);
}
