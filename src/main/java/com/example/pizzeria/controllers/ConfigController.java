package com.example.pizzeria.controllers;

import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.services.PizzeriaConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/config")
public class ConfigController {
    private final PizzeriaConfigService pizzeriaConfigService;

    public ConfigController(PizzeriaConfigService pizzeriaConfigService) {
        this.pizzeriaConfigService = pizzeriaConfigService;
    }

    @GetMapping("/pizza-stages")
    public ResponseEntity<List<PizzaStage>> getPizzaStages(){
        return  ResponseEntity.ok(pizzeriaConfigService.getPizzaStages());
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Recipe>> getMenu() {
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
}
