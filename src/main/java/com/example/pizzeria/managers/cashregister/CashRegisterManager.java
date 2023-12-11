package com.example.pizzeria.managers.cashregister;

import com.example.pizzeria.events.ServiceOrderUpdateEvent;
import com.example.pizzeria.managers.cooking.ICookingManager;
import com.example.pizzeria.models.Diner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Setter
@Getter
public class CashRegisterManager implements ICashRegisterManager {
    private final ApplicationEventPublisher publisher;

    private List<CashRegister> cashRegisters;

    @Override
    public void acceptDinner(Diner diner) throws IllegalStateException {
        if (this.cashRegisters.isEmpty()) {
            throw new IllegalStateException("No cash registers available");
        }
        CashRegister cashRegister = this.cashRegisters.stream()
                .min(Comparator.comparingInt(cr -> cr.getDiners().size()))
                .orElseThrow();
        publisher.publishEvent(new ServiceOrderUpdateEvent(this, cashRegister, diner.getOrder()));

        cashRegister.addDinner(diner);

    }

    @Override
    public void init(int cashRegistersNumber, ICookingManager cookingManager) {
        cashRegisters = new ArrayList<>();
        for (int i = 0; i < cashRegistersNumber; i++) {
            CashRegister cashRegister = new CashRegister(cookingManager);
            cashRegisters.add(cashRegister);
        }
    }

    @Override
    public void terminate() {
        cashRegisters.clear();
    }

}
