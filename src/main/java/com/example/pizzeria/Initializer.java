package com.example.pizzeria;

import com.example.pizzeria.managers.cashregister.CashRegister;
import com.example.pizzeria.managers.cashregister.CashRegisterManager;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.managers.cooking.SpecializedCookingManager;
import com.example.pizzeria.managers.cooking.UniversalCookingManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        initCashRegisterManager(cookingManager);
    }

    private void initCashRegisterManager(ICookingManager cookingManager) {
        List<CashRegister> cashRegisters = new ArrayList<>();

        for (int i = 0; i < config.getCashRegisterQuantity(); i++) {
            CashRegister cashRegister = new CashRegister(cookingManager);
            cashRegisters.add(cashRegister);
        }
        cashRegisterManager.setCashRegisters(cashRegisters);
    }
}

