package com.example.pizzeria.managers.cashregister;

import com.example.pizzeria.models.Diner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Setter
@Getter
public class CashRegisterManager implements ICashRegisterManager {
    private List<CashRegister> cashRegisters;

    @Override
    public void acceptDinner(Diner diner) throws IllegalStateException {
        if (this.cashRegisters.isEmpty()) {
            throw new IllegalStateException("No cash registers available");
        }
        CashRegister cashRegister = this.cashRegisters.stream()
                .min(Comparator.comparingInt(cr -> cr.getDiners().size()))
                .orElseThrow();
        cashRegister.addDinner(diner);
    }

}
