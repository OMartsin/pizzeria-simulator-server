package com.example.pizzeria.controllers;

import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.services.IPizzeriaConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/config")
public class ConfigController {
    private final IPizzeriaConfigService pizzeriaConfigService;

    public ConfigController(IPizzeriaConfigService pizzeriaConfigService) {
        this.pizzeriaConfigService = pizzeriaConfigService;
    }

    @GetMapping("/pizza-stages")
    public ResponseEntity<List<PizzaStage>> getPizzaStages(){
        return  ResponseEntity.ok(pizzeriaConfigService.getPizzaStages());
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Recipe>> getMenu() throws IOException {
        return ResponseEntity.ok(pizzeriaConfigService.getMenu());
    }
    @GetMapping()
    public ResponseEntity<PizzeriaConfig> getPizzeriaConfig() {
        return ResponseEntity.ok(pizzeriaConfigService.getPizzeriaConfig());
    }

    @PutMapping()
    public ResponseEntity<PizzeriaConfig> setPizzeriaConfig
            (@RequestBody PizzeriaConfigInputDto config) {
        pizzeriaConfigService.setPizzeriaConfig(pizzeriaConfigService.mapToPizzeriaConfig(config));
        return  ResponseEntity.ok(pizzeriaConfigService.getPizzeriaConfig());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException ex) {
        // Handle the exception and return an error response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error reading pizza menu from file");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
