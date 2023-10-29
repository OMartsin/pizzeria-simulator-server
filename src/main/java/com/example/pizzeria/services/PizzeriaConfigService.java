package com.example.pizzeria.services;

import com.example.pizzeria.PizzaMenuReader;
import com.example.pizzeria.PizzeriaConfigMapper;
import com.example.pizzeria.config.DinerArrivalConfig;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PizzeriaConfigService implements IPizzeriaConfigService {
    private final PizzeriaConfig pizzeriaConfig;
    private final PizzeriaConfigMapper configMapper;
    private final PizzaMenuReader pizzaMenuReader;
    @Override
    public boolean getSpecializedCooksMode() {
        return pizzeriaConfig.isSpecializedCooksMode();
    }

    @Override
    public Map<PizzaStage, Integer> getCookStageDurationMap() {
        return pizzeriaConfig.getCooksPerStage();
    }

    @Override
    public DinerArrivalConfig getDinerArrivalConfig() {
        return pizzeriaConfig.getDinerArrivalConfig();
    }

    @Override
    public int getCashRegisterQuantity() {
        return pizzeriaConfig.getCashRegisterQuantity();
    }

    @Override
    public List<Recipe> getAvailableRecipes()    {
        return pizzeriaConfig.getMenu();
    }

    @Override
    public List<Recipe> getMenu() {
        return pizzaMenuReader.getAllRecipes();
    }

    @Override
    public Map<PizzaStage, Integer> getCooksPerStage() {
        return pizzeriaConfig.getCooksPerStage();
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
    public void setSpecializedCooksMode(boolean specializedCooksMode) {
        pizzeriaConfig.setSpecializedCooksMode(specializedCooksMode);
    }

    @Override
    public void setCookStageDurationMap(Map<PizzaStage, Integer> cookStageDurationMap) {
        pizzeriaConfig.setCooksPerStage(cookStageDurationMap);
    }

    @Override
    public void setDinerArrivalConfig(DinerArrivalConfig dinerArrivalConfig) {
        pizzeriaConfig.setDinerArrivalConfig(dinerArrivalConfig);
    }

    @Override
    public void setCashRegisterQuantity(int cashRegisterQuantity) {
        pizzeriaConfig.setCashRegisterQuantity(cashRegisterQuantity);
    }

    @Override
    public void setAvailableRecipes(List<Recipe> menu) {
        pizzeriaConfig.setMenu(menu);
    }

    @Override
    public void setCooksPerStage(Map<PizzaStage, Integer> cooksPerStage) {
        pizzeriaConfig.setCooksPerStage(cooksPerStage);
    }

    @Override
    public void setPizzeriaConfig(PizzeriaConfig config) {
        pizzeriaConfig.setSpecializedCooksMode(config.isSpecializedCooksMode());
        pizzeriaConfig.setCookStageDurationMap(config.getCookStageDurationMap());
        pizzeriaConfig.setCooksPerStage(config.getCooksPerStage());
        pizzeriaConfig.setDinerArrivalConfig(config.getDinerArrivalConfig());
        pizzeriaConfig.setCashRegisterQuantity(config.getCashRegisterQuantity());
        pizzeriaConfig.setMenu(config.getMenu());
    }

    @Override
    public PizzeriaConfig mapToPizzeriaConfig(PizzeriaConfigInputDto inputDto) {
        return configMapper.toPizzeriaConfig(inputDto);
    }

}
