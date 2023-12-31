package com.example.pizzeria;

import com.example.pizzeria.managers.cashregister.CashRegisterManager;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.managers.cooking.SpecializedCookingManager;
import com.example.pizzeria.managers.cooking.UniversalCookingManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Initializer {
    private final PizzeriaConfig config;
    private final DinersGenerator dinersGenerator;
    private final CashRegisterManager cashRegisterManager;
    private final SpecializedCookingManager specializedCookingManager;
    private final UniversalCookingManager universalCookingManager;

    public void init() {
        dinersGenerator.setDinerArrivalConfig(config);
        ICookingManager cookingManager = config.isSpecializedCooksMode()
                ? specializedCookingManager : universalCookingManager;
        cookingManager.init();
        cashRegisterManager.init(config.getCashRegisterQuantity(), cookingManager);
    }

    public void terminate() {
        dinersGenerator.terminate();
        specializedCookingManager.terminate();
        universalCookingManager.terminate();
        cashRegisterManager.terminate();
    }
}

