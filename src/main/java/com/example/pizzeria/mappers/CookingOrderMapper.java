package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.CookingOrderDto;
import com.example.pizzeria.events.PostCookingOrderUpdateEvent;
import com.example.pizzeria.events.PreCookingOrderUpdateEvent;
import com.example.pizzeria.models.PizzaStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CookingOrderMapper {
    CookingOrderMapper INSTANCE = Mappers.getMapper(CookingOrderMapper.class);

    @Mapping(source = "pizzaCookingState.currPizzaStage", target = "currentStage")
    @Mapping(source = "cook.cookId", target = "cookId")
    @Mapping(source = "pizzaCookingState.orderId", target = "orderId")
    @Mapping(source = "pizzaCookingState.orderedItem.id", target = "orderPizzaId")
    @Mapping(source = "pizzaCookingState.completedAt", target = "completedAt")
    @Mapping(target = "currentTopping", qualifiedByName = "mapCurrentTopping", source = "event")
    @Mapping(source = "pizzaCookingState.modifiedAt", target = "modifiedAt")
    CookingOrderDto toCookingOrderDto(PreCookingOrderUpdateEvent event);

    @Named("mapCurrentTopping")
    default String mapCurrentTopping(PreCookingOrderUpdateEvent event) {
        if (event.getPizzaCookingState().getCurrPizzaStage().equals(PizzaStage.Topping)) {
            return event.getPizzaCookingState().getNextTopping();
        } else {
            return null;
        }
    }

    @Mapping(source = "pizzaCookingState.currPizzaStage", target = "currentStage")
    @Mapping(source = "cook.cookId", target = "cookId")
    @Mapping(source = "pizzaCookingState.orderId", target = "orderId")
    @Mapping(source = "pizzaCookingState.orderedItem.id", target = "orderPizzaId")
    @Mapping(source = "pizzaCookingState.completedAt", target = "completedAt")
    @Mapping(source = "pizzaCookingState.currentTopping", target = "currentTopping")
    @Mapping(source = "pizzaCookingState.modifiedAt", target = "modifiedAt")
    CookingOrderDto toCookingOrderDto(PostCookingOrderUpdateEvent event);
}
