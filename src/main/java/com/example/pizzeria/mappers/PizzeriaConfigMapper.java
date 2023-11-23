package com.example.pizzeria.mappers;

import com.example.pizzeria.PizzaMenuReader;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.PizzaStage;
import com.example.pizzeria.models.Recipe;
import com.example.pizzeria.dto.PizzeriaConfigInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PizzeriaConfigMapper {
    PizzeriaConfigMapper INSTANCE = Mappers.getMapper(PizzeriaConfigMapper.class);

    @Mapping(target = "menu", source = "menu", qualifiedByName = "mapMenu")
    @Mapping(target = "cooksPerStage", source = "inputDto", qualifiedByName = "mapCooksPerStage")
    @Mapping(target = "cooksNumber", source = "inputDto", qualifiedByName = "mapCooksNumber")
    PizzeriaConfig toPizzeriaConfig(PizzeriaConfigInputDto inputDto);

    @Named("mapMenu")
    default List<Recipe> mapMenu(List<Integer> menuIds) throws IOException {
        PizzaMenuReader menuReader = new PizzaMenuReader();
        List<Recipe> allRecipes = menuReader.getAllRecipes();

        return allRecipes.stream()
                .filter(recipe -> menuIds.contains(recipe.getId()))
                .collect(Collectors.toList());
    }

    @Named("mapCooksPerStage")
    default Map<PizzaStage, Integer> mapCooksPerStage(PizzeriaConfigInputDto inputDto) {
        if (inputDto.specializedCooksMode()) {
            return inputDto.cooksPerStage();
        } else {
            return null;
        }
    }

    @Named("mapCooksNumber")
    default Integer mapCooksNumber(PizzeriaConfigInputDto inputDto) throws IllegalArgumentException {
        if (inputDto.specializedCooksMode()) {
            return inputDto.cooksPerStage().values().stream().mapToInt(Integer::intValue).sum();
        } else {
            if(inputDto.cooksNumber() <= 0){
                throw new IllegalArgumentException("Cooks number must be greater than 0");
            }
            return inputDto.cooksNumber();
        }
    }
}
