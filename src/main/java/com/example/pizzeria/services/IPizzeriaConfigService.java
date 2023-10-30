package com.example.pizzeria.services;

import com.example.pizzeria.config.DinerArrivalConfig;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPizzeriaConfigService {
    boolean getSpecializedCooksMode();
    Map<PizzaStage, Integer> getCookStageDurationMap();
    DinerArrivalConfig getDinerArrivalConfig();
    int getCashRegisterQuantity();
    List<Recipe> getAvailableRecipes();
    List<Recipe> getMenu() throws IOException;
    Map<PizzaStage, Integer> getCooksPerStage();
    PizzeriaConfig getPizzeriaConfig();
    List<PizzaStage> getPizzaStages();
    void setSpecializedCooksMode(boolean specializedCooksMode);
    void setCookStageDurationMap(Map<PizzaStage, Integer> cookStageDurationMap);
    void setDinerArrivalConfig(DinerArrivalConfig dinerArrivalConfig);
    void setCashRegisterQuantity( int cashRegisterQuantity);
    void setAvailableRecipes(List<Recipe> menu);
    void setCooksPerStage(Map<PizzaStage, Integer> cooksPerStage);
    void setPizzeriaConfig(PizzeriaConfig config);
    PizzeriaConfig mapToPizzeriaConfig(PizzeriaConfigInputDto inputDto);

}
