package com.tim.foodwastetracker.dto;

import com.tim.foodwastetracker.model.FoodCategory;
import com.tim.foodwastetracker.model.Unit;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FoodItemResponse(
        Long foodId,
        String productName,
        FoodCategory foodCategory,
        LocalDate expirationDate,
        Double quantity,
        Unit unit,
        String brand) {
}
