package com.tim.foodwastetracker.dto;

import com.tim.foodwastetracker.model.FoodItem;
import com.tim.foodwastetracker.model.Unit;
import com.tim.foodwastetracker.model.WasteReason;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record FoodWasteRecordResponse(
        Long recordId,
        LocalDate wasteDate,
        Double quantity,
        Unit unit,
        WasteReason reason,
        String location,
        LocalDate createdAt,
        FoodItem foodItem) {
}
