package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.PauseCookDto;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PauseCookUpdateEventMapper {

    PauseCookUpdateEventMapper INSTANCE = Mappers.getMapper(PauseCookUpdateEventMapper.class);

    @Mapping(source = "cook.cookId", target = "cookId")
    @Mapping(source = "cook.status", target = "cookStatus")
    PauseCookDto toPauseCookDto(PausedCookUpdateEvent event);
}
