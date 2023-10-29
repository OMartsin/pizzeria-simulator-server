package com.example.pizzeria.PizzeriaConfig;

import java.util.List;
import java.util.Map;

public interface IPizzeriaConfigService {
    boolean getSpecializedCooksMode();
    Map<PizzaStage, Integer> getCookStageDurationMap();
    DinerArrivalConfig getDinerArrivalConfig();
    int getCashRegisterQuantity();
    Recipe[] getMenu();
    Map<PizzaStage, Integer> getCooksPerStage();
    PizzeriaConfig getPizzeriaConfig();
    List<PizzaStage> getPizzaStages();
    void setSpecializedCooksMode(boolean specializedCooksMode);
    void setCookStageDurationMap(Map<PizzaStage, Integer> cookStageDurationMap);
    void setDinerArrivalConfig(DinerArrivalConfig dinerArrivalConfig);
    void setCashRegisterQuantity( int cashRegisterQuantity);
    void setMenu(Recipe[] menu);
    void setCooksPerStage(Map<PizzaStage, Integer> cooksPerStage);
    void setPizzeriaConfig(PizzeriaConfig config);



}
