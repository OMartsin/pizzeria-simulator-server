package com.example.pizzeria.controllers;

import com.example.pizzeria.services.KitchenStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchen-state")
@RequiredArgsConstructor
public class KitchenStateController {
    private final KitchenStateService kitchenStateService;

    @GetMapping()
    public Object getKitchenState() {
        return kitchenStateService.getKitchenState();
    }
}
