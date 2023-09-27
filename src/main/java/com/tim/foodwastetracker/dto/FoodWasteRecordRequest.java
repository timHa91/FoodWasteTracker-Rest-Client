package com.tim.foodwastetracker.dto;

import com.tim.foodwastetracker.model.Unit;
import com.tim.foodwastetracker.model.WasteReason;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Builder
public record FoodWasteRecordRequest(
        @NotNull(message = "Waste Date cannot be null")
        LocalDate wasteDate,
        @NotNull(message = "Quantity cannot be null")
        Double quantity,
        @NotNull(message = "Unit cannot be null")
        Unit unit,
        @NotNull(message = "Waste Reason cannot be null")
        WasteReason reason,
        @NotBlank(message = "Location cannot be blank")
        String location,
        @NotNull(message = "Food Item Id cannot be null")
        Long foodItemId) {
}

