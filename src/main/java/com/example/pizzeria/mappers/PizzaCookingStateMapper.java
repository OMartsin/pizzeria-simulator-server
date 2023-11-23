package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.PizzaCookingStateDto;
import com.example.pizzeria.models.PizzaCookingState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PizzaCookingStateMapper {

    PizzaCookingStateMapper INSTANCE = Mappers.getMapper(PizzaCookingStateMapper.class);

    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "currTopping", expression = "java(getToppingName(state))")
    PizzaCookingStateDto toPizzaCookingStateDto(PizzaCookingState state);

    default String getToppingName(PizzaCookingState state) {
        if(state.getCurrToppingIndex() == null)
            return null;
        return state.getRecipe().getToppings().get(state.getCurrToppingIndex());
    }
}
