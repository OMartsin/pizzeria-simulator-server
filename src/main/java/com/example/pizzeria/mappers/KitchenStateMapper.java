package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.KitchenStateDto;
import com.example.pizzeria.dto.CookDto;
import com.example.pizzeria.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KitchenStateMapper {
    KitchenStateMapper INSTANCE = Mappers.getMapper(KitchenStateMapper.class);

    KitchenStateDto toKitchenStateDto(List<CookDto> cooks, List<OrderDto> orders);

}