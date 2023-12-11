package com.example.pizzeria.services;

import com.example.pizzeria.dto.KitchenStateDto;
import com.example.pizzeria.managers.cooking.SpecializedCookingManager;
import com.example.pizzeria.managers.cooking.UniversalCookingManager;
import com.example.pizzeria.mappers.CookMapper;
import com.example.pizzeria.mappers.KitchenStateMapper;
import com.example.pizzeria.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KitchenStateService {
    private final SpecializedCookingManager specializedCookingManager;
    private final UniversalCookingManager universalCookingManager;
    private final KitchenStateMapper kitchenStateMapper;
    private final CookMapper cookMapper;
    private final OrderMapper orderMapper;

    public KitchenStateDto getKitchenState() {
        if(specializedCookingManager.getCooks() != null && !specializedCookingManager.getCooks().isEmpty())
            return kitchenStateMapper.toKitchenStateDto(
                    cookMapper.mapCooksWithSpec(specializedCookingManager.getCooks(),
                            specializedCookingManager.getCookPerStage()),
                    orderMapper.mapOrders(specializedCookingManager.getOrders())
            );
        else if(universalCookingManager.getCooks() != null && !universalCookingManager.getCooks().isEmpty())
            return kitchenStateMapper.toKitchenStateDto(
                    cookMapper.mapCooks(universalCookingManager.getCooks()),
                    orderMapper.mapOrders(universalCookingManager.getOrders())
            );
        else
            throw new RuntimeException("Simulation not started");
    }
}
