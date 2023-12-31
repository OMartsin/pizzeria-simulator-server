package com.example.pizzeria.controllers;

import com.example.pizzeria.dto.KitchenStateDto;
import com.example.pizzeria.services.KitchenStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kitchen-state")
@RequiredArgsConstructor
public class KitchenStateController {
    private final KitchenStateService kitchenStateService;

    @GetMapping()
    public ResponseEntity<KitchenStateDto> getKitchenState() {
        return ResponseEntity.ok(kitchenStateService.getKitchenState());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kebab request: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
