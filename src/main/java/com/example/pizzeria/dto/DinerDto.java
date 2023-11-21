package com.example.pizzeria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DinerDto {
    private int id;
    private String name;

    public DinerDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
