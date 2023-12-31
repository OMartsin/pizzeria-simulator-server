package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.OrderDto;
import com.example.pizzeria.dto.PizzaCookingStateDto;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PizzaCookingStateMapper.class)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "createdAt", source = "order.orderTime")
    @Mapping(target = "orderPizzas", source = "pizzaCookingStates")
    OrderDto toOrderDto(Order order, List<PizzaCookingState> pizzaCookingStates);

    @IterableMapping(elementTargetType = PizzaCookingStateDto.class)
    List<PizzaCookingStateDto> mapPizzaCookingStateList(List<PizzaCookingState> pizzaCookingStates);


    default List<OrderDto> mapOrders(Map<Order, List<PizzaCookingState>> orderMap) {
        return orderMap.entrySet().stream()
                .map(entry -> toOrderDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}