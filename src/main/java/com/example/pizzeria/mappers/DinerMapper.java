package com.example.pizzeria.mappers;

import com.example.pizzeria.dto.DinerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DinerMapper {

    DinerMapper INSTANCE = Mappers.getMapper(DinerMapper.class);

    DinerDto toDinerDto(Integer id, String name);

}
