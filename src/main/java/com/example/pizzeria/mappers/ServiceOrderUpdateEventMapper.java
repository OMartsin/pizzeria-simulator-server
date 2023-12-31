package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.OrderPizzaDto;
import com.example.pizzeria.dto.ServiceOrderDto;
import com.example.pizzeria.events.ServiceOrderUpdateEvent;
import com.example.pizzeria.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = DinerMapper.class)
public interface ServiceOrderUpdateEventMapper {

    ServiceOrderUpdateEventMapper INSTANCE = Mappers.getMapper(ServiceOrderUpdateEventMapper.class);
    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "cashRegister.id", target = "cashRegisterId")
    @Mapping(source = "order.orderTime", target = "createdAt")
    @Mapping(source = "order", target = "orderPizzas", qualifiedByName = "toOrderPizzaDtoList")
    @Mapping(source = "order.diner", target = "diner")
    ServiceOrderDto toServiceOrderDto(ServiceOrderUpdateEvent event);

    @Named("toOrderPizzaDtoList")
    default List<OrderPizzaDto> toOrderPizzaDtoList(Order order) {
        return order.getOrderedItems().stream()
                .map(orderedItem -> new OrderPizzaDto(orderedItem.getId(), order.getId(),
                        orderedItem.getRecipe().getId()))
                .toList();
    }
}
