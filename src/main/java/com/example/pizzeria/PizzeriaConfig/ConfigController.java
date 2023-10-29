package com.example.pizzeria.PizzeriaConfig;

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
    @GetMapping()
    public ResponseEntity<PizzeriaConfig> getPizzeriaConfig() {
        return ResponseEntity.ok(pizzeriaConfigService.getPizzeriaConfig());
    }

    @PutMapping()
    public ResponseEntity<PizzeriaConfig> setPizzeriaConfig
            (@RequestBody PizzeriaConfig config) {
        pizzeriaConfigService.setPizzeriaConfig(config);
        return  ResponseEntity.ok(pizzeriaConfigService.getPizzeriaConfig());

    }


}
