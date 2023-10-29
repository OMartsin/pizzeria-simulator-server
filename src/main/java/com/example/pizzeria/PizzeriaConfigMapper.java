package com.example.pizzeria;

import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PizzeriaConfigMapper {
    PizzeriaConfigMapper INSTANCE = Mappers.getMapper(PizzeriaConfigMapper.class);

    @Mapping(target = "menu", source = "menu")
    PizzeriaConfig toPizzeriaConfig(PizzeriaConfigInputDto inputDto);

    default List<Recipe> mapMenu(List<Integer> menuIds) {
        PizzaMenuReader menuReader;
        menuReader = new PizzaMenuReader();
        List<Recipe> allRecipes = menuReader.getAllRecipes();

        return allRecipes.stream()
                .filter(recipe -> menuIds.contains(recipe.getId()))
                .collect(Collectors.toList());
    }

}
