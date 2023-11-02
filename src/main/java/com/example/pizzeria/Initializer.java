package com.example.pizzeria;

import com.example.pizzeria.config.PizzeriaConfig;
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
    public void init() {
        dinersGenerator.setDinerArrivalConfig(config);
        initCashRegisterManager();
    }

    private void initCashRegisterManager(){
        List<CashRegister> cashRegisters = new ArrayList<>();

        for (int i = 0; i < config.getCashRegisterQuantity(); i++) {
            CashRegister cashRegister = new CashRegister();
            cashRegisters.add(cashRegister);
        }
        cashRegisterManager.setCashRegisters(cashRegisters);
    }
}
