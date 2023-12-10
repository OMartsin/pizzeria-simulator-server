package com.example.pizzeria.dto;

import com.example.pizzeria.models.cook.CookStatus;

public record PauseCookDto(int cookId, CookStatus cookStatus) {}
