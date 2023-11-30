package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.PizzaCookingStateDto;
import com.example.pizzeria.models.PizzaCookingState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PizzaCookingStateMapper {

    PizzaCookingStateMapper INSTANCE = Mappers.getMapper(PizzaCookingStateMapper.class);
    @Mapping(target = "id", source = "orderedItem.id")
    @Mapping(target = "recipeId", source = "orderedItem.recipe.id")
    @Mapping(target = "currentTopping", expression = "java(getToppingName(state))")
    @Mapping(target = "currentStage", source = "currStage")
    @Mapping(target = "completedAt", source = "completedAt")
    PizzaCookingStateDto toPizzaCookingStateDto(PizzaCookingState state);

    default String getToppingName(PizzaCookingState state) {
        if(state.getCurrToppingIndex() == null)
            return null;
        return state.getOrderedItem().getRecipe().getToppings().get(state.getCurrToppingIndex());
    }
}
