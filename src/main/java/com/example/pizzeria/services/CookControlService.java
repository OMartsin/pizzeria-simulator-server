package com.example.pizzeria.services;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.managers.cooking.SpecializedCookingManager;
import com.example.pizzeria.managers.cooking.UniversalCookingManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookControlService {
    private final UniversalCookingManager universalCookingManager;
    private final SpecializedCookingManager specializedCookingManager;
    private final PizzeriaConfig config;

    public void pauseCook(Integer cookId) {
        getCookingManager().pauseCook(cookId);
    }

    public void resumeCook(Integer cookId) {
        getCookingManager().resumeCook(cookId);
    }

    private ICookingManager getCookingManager() {
        return config.isSpecializedCooksMode()
                ? specializedCookingManager : universalCookingManager;
    }

}
