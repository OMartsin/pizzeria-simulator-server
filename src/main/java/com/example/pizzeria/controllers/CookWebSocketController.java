package com.example.pizzeria.controllers;

import com.example.pizzeria.services.CookControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CookWebSocketController {
    private final CookControlService cookControlService;

    @MessageMapping("/cook/pause")
    public void pauseCook(@Payload Integer cookId) {
        cookControlService.pauseCook(cookId);
    }

    @MessageMapping("/cook/resume")
    public void resumeCook(@Payload Integer cookId) {
        cookControlService.resumeCook(cookId);
    }

}