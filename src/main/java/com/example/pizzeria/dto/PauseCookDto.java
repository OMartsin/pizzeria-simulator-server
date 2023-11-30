package com.example.pizzeria.dto;

import com.example.pizzeria.models.cook.CookStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PauseCookDto {

        private int cookId;
        private CookStatus cookStatus;


        public PauseCookDto(int cookId, CookStatus cookStatus) {
                this.cookId = cookId;
                this.cookStatus = cookStatus;
        }
}
