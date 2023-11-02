package com.example.pizzeria.services;

import com.example.pizzeria.PizzaMenuReader;
import com.example.pizzeria.config.PizzeriaConfigMapper;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzeriaConfigService implements IPizzeriaConfigService {
    private final PizzeriaConfig pizzeriaConfig;
    private final PizzeriaConfigMapper configMapper;
    private final PizzaMenuReader pizzaMenuReader;
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
        return configMapper.toPizzeriaConfig(inputDto);
    }

}
