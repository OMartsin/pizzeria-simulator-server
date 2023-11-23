package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.DinerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DinerMapper {
    @SuppressWarnings("unused")
    DinerDto toDinerDto(Integer id, String name);

}
