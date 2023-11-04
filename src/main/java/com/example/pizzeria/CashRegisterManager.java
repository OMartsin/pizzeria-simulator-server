package com.example.pizzeria;

import com.example.pizzeria.models.Dinner;
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
    public void acceptDinner(Dinner dinner) throws IllegalStateException {
        if (this.cashRegisters.isEmpty()) {
            throw new IllegalStateException("No cash registers available");
        }
        CashRegister cashRegister = this.cashRegisters.stream()
                .min(Comparator.comparingInt(cr -> cr.getDinners().size()))
                .orElseThrow();
        cashRegister.addDinner(dinner);
    }

}
