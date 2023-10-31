package com.example.pizzeria.config;

import com.example.pizzeria.PizzaMenuReader;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PizzeriaConfigMapper {
    PizzeriaConfigMapper INSTANCE = Mappers.getMapper(PizzeriaConfigMapper.class);

    @Mapping(target = "menu", source = "menu")
    PizzeriaConfig toPizzeriaConfig(PizzeriaConfigInputDto inputDto);

    default List<Recipe> mapMenu(List<Integer> menuIds) throws IOException {
        PizzaMenuReader menuReader = new PizzaMenuReader();
        List<Recipe> allRecipes;
        allRecipes = menuReader.getAllRecipes();

        return allRecipes.stream()
                .filter(recipe -> menuIds.contains(recipe.getId()))
                .collect(Collectors.toList());
    }

}
