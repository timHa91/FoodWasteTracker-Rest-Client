package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.dto.FoodWasteRecordRequest;
import com.tim.foodwastetracker.dto.FoodWasteRecordResponse;
import com.tim.foodwastetracker.exception.FoodItemNotFoundException;
import com.tim.foodwastetracker.exception.RecordNotFoundException;
import com.tim.foodwastetracker.model.FoodItem;
import com.tim.foodwastetracker.model.FoodWasteRecord;
import com.tim.foodwastetracker.repository.FoodItemRepository;
import com.tim.foodwastetracker.repository.FoodWasteRecordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class FoodWasteRecordService {

    private final FoodWasteRecordRepository foodWasteRecordRepository;
    private final FoodItemRepository foodItemRepository;
    private final AuthenticationService authenticationService;

    // Having control over the clock for mocks
    // private final Clock clock;

    public List<FoodWasteRecordResponse> getAllRecords() {
        log.info("Fetching all Food Waste Records from the database");
        return foodWasteRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FoodWasteRecordResponse getRecordById(Long id) {
        log.info("Fetching Food Waste Record with ID: {} from the database", id);
        var foodWasteRecord = foodWasteRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Record not found"));
        return mapToResponse(foodWasteRecord);
    }

    public FoodWasteRecordResponse createRecord(FoodWasteRecordRequest request) {
        log.info("Creating a new Food Waste Record for FoodItem ID: {}", request.foodItemId());
        var foodItem = foodItemRepository.findById(request.foodItemId())
                        .orElseThrow(() -> new FoodItemNotFoundException("Food Item not found"));
        var foodWasteRecord = foodWasteRecordRepository.save(buildNewRecord(foodItem, request));
        return mapToResponse(foodWasteRecord);
    }

    private FoodWasteRecordResponse mapToResponse(FoodWasteRecord foodWasteRecord) {
        return FoodWasteRecordResponse.builder()
                .recordId(foodWasteRecord.getRecordId())
                .quantity(foodWasteRecord.getQuantity())
                .wasteDate(foodWasteRecord.getWasteDate())
                .createdAt(foodWasteRecord.getCreatedAt())
                .foodItem(foodWasteRecord.getFoodItem())
                .unit(foodWasteRecord.getUnit())
                .location(foodWasteRecord.getLocation())
                .reason(foodWasteRecord.getReason())
                .build();
    }

    private FoodWasteRecord buildNewRecord(FoodItem foodItem, FoodWasteRecordRequest request) {
       var user = authenticationService.getCurrentUser();
        return FoodWasteRecord.builder()
                .foodItem(foodItem)
                .wasteDate(LocalDate.now())
                .createdAt(LocalDate.now())
                .location(request.location())
                .quantity(request.quantity())
                .unit(request.unit())
                .reason(request.reason())
                .user(user)
                .build();
    }
}
