package com.example.pizzeria.controllers;

import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.services.IPizzeriaConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get pizza cooking stages")
    public ResponseEntity<List<PizzaStage>> getPizzaStages(){
        return  ResponseEntity.ok(pizzeriaConfigService.getPizzaStages());
    }

    @GetMapping("/menu")
    @Operation(summary = "Get pizza menu")
    public ResponseEntity<List<Recipe>> getMenu() throws IOException {
        return ResponseEntity.ok(pizzeriaConfigService.getMenu());
    }
    @GetMapping()
    @Operation(summary = "Get pizzeria config")
    public ResponseEntity<PizzeriaConfig> getPizzeriaConfig() {
        return ResponseEntity.ok(pizzeriaConfigService.getPizzeriaConfig());
    }

    @PutMapping()
    @Operation(summary = "Update pizzeria config")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Config updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PizzeriaConfig.class))),
            @ApiResponse(responseCode = "400", description = "Cooks number must be greater than 0",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error reading pizza menu from file",
                    content = @Content) })
    public ResponseEntity<PizzeriaConfig> setPizzeriaConfig
            (@RequestBody PizzeriaConfigInputDto config) throws IOException, IllegalArgumentException {
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Internal server error" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
