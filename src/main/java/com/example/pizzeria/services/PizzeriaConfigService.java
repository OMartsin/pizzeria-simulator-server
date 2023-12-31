package com.example.pizzeria.services;

import com.example.pizzeria.PizzaMenuReader;
import com.example.pizzeria.config.DinerArrivalConfig;
import com.example.pizzeria.config.DinerArrivalFrequency;
import com.example.pizzeria.mappers.PizzeriaConfigMapper;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PizzeriaConfigService implements IPizzeriaConfigService {
    private final PizzeriaConfig pizzeriaConfig;
    private final PizzeriaConfigMapper configMapper;
    private final PizzaMenuReader pizzaMenuReader;

    @PostConstruct
    public void initDefaultConfig() throws IOException {
        pizzeriaConfig.updateConfig(
                true,
                new HashMap<>() {
                    {
                        put(PizzaStage.Dough, 0.3);
                        put(PizzaStage.Topping, 0.2);
                        put(PizzaStage.Baking, 0.4);
                        put(PizzaStage.Packaging, 0.1);
                    }
                },
                100,
                new DinerArrivalConfig(DinerArrivalFrequency.Medium, 2),
                3,
                new ArrayList<>(getMenu()
                        .stream()
                        .limit(3)
                        .collect(Collectors.toList())),
                new HashMap<>() {
                    {
                        put(PizzaStage.Dough, 3);
                        put(PizzaStage.Topping, 4);
                        put(PizzaStage.Baking, 4);
                        put(PizzaStage.Packaging, 2);
                    }
                },
                13
        );
    }

    @Override
    public List<Recipe> getMenu() throws IOException {
        return pizzaMenuReader.getAllRecipes();
    }

    @Override
    public PizzeriaConfig getPizzeriaConfig() {
        return pizzeriaConfig;
    }

    @Override
    public List<PizzaStage> getPizzaStages() {
        return List.of(PizzaStage.values());
    }

    @Override
    public void setPizzeriaConfig(PizzeriaConfig config) {
        pizzeriaConfig.updateConfig(config);
    }

    @Override
    public PizzeriaConfig mapToPizzeriaConfig(PizzeriaConfigInputDto inputDto) throws IOException,
            IllegalArgumentException {
        var config = configMapper.toPizzeriaConfig(inputDto);
        config.setPizzaStagesTimeCoeffs(pizzeriaConfig.getPizzaStagesTimeCoeffs());
        return config;
    }

}
