package com.tim.foodwastetracker.dto;

import com.tim.foodwastetracker.model.FoodCategory;
import com.tim.foodwastetracker.model.Unit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record FoodItemRequest(
        @NotBlank(message = "Product Name cannot be blank")
        String productName,
        @NotNull(message = "Food Category cannot be null")
        FoodCategory foodCategory,
        @NotNull(message = "Expiration Date cannot be null")
        LocalDate expirationDate,
        @NotNull(message = "Quantity cannot be null")
        Double quantity,
        @NotNull(message = "Unit cannot be null")
        Unit unit,
        @NotBlank(message = "Brand cannot be blank")
        String brand) {
}
