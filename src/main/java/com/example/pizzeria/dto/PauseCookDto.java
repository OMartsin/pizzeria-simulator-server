package com.example.pizzeria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PauseCookDto {

        private int cookId;

        public PauseCookDto(int cookId) {
                this.cookId = cookId;
        }
}
