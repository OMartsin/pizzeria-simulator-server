package com.example.pizzeria.services;

import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;

import java.io.IOException;
import java.util.List;

public interface IPizzeriaConfigService {
    List<Recipe> getMenu() throws IOException;
    PizzeriaConfig getPizzeriaConfig();
    List<PizzaStage> getPizzaStages();
    void setPizzeriaConfig(PizzeriaConfig config);
    PizzeriaConfig mapToPizzeriaConfig(PizzeriaConfigInputDto inputDto) throws IOException, IllegalArgumentException;

}
