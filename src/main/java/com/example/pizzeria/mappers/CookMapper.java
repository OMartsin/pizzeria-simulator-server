package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.CookDto;
import com.example.pizzeria.models.PizzaCookingState;
import com.example.pizzeria.models.cook.Cook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.example.pizzeria.models.PizzaStage;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CookMapper {
    CookMapper INSTANCE = Mappers.getMapper(CookMapper.class);

    @Mapping(target = "id", source = "cook.cookId")
    @Mapping(target = "name", source = "cook.cookName")
    @Mapping(target = "status", source = "cook.status")
    @Mapping(target = "specialization", source = "specialization")
    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "orderPizzaId", source = "orderPizzaId")
    CookDto toCookDto(Cook cook, PizzaStage specialization, Integer orderId, Integer orderPizzaId);

    default List<CookDto> mapCooks(Map<Cook, PizzaCookingState> cookStateMap) {
        return cookStateMap.entrySet().stream()
                .map(entry -> toCookDto(entry.getKey(),
                        null,
                        entry.getValue().getOrderId(),
                        entry.getValue().getId()))
                .collect(Collectors.toList());
    }
}
