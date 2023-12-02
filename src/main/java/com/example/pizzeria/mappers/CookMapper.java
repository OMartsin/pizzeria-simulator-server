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
                .map(entry -> {
                    PizzaCookingState state = entry.getValue();
                    Integer orderId = state != null ? state.getOrderId() : null;
                    Integer orderPizzaId = state != null ? state.getOrderedItem().getId() : null;
                    return toCookDto(entry.getKey(), null, orderId, orderPizzaId);
                })
                .collect(Collectors.toList());
    }

    default List<CookDto> mapCooksWithSpec(Map<Cook, PizzaCookingState> cookStateMap,
                                           Map<PizzaStage, List<Cook>> stageCookMap) {
        return cookStateMap.entrySet().stream()
                .map(entry -> {
                    PizzaCookingState state = entry.getValue();
                    PizzaStage specialization = findSpecializationForCook(entry.getKey(), stageCookMap);
                    Integer orderId = state != null ? state.getOrderId() : null;
                    Integer orderPizzaId = state != null ? state.getOrderedItem().getId() : null;
                    return toCookDto(entry.getKey(), specialization, orderId, orderPizzaId);
                })
                .collect(Collectors.toList());
    }

    default PizzaStage findSpecializationForCook(Cook cook, Map<PizzaStage, List<Cook>> stageCookMap) {
        return stageCookMap.entrySet().stream()
                .filter(entry -> entry.getValue().contains(cook))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null); // or any default value you prefer
    }
}
