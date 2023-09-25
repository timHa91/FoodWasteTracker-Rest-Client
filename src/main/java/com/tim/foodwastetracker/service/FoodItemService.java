package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.dto.FoodItemRequest;
import com.tim.foodwastetracker.dto.FoodItemResponse;
import com.tim.foodwastetracker.dto.FoodWasteRecordRequest;
import com.tim.foodwastetracker.exception.FoodItemNotFoundException;
import com.tim.foodwastetracker.model.FoodItem;
import com.tim.foodwastetracker.model.FoodWasteRecord;
import com.tim.foodwastetracker.repository.FoodItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final AuthenticationService authenticationService;

    public List<FoodItemResponse> getAllFoodItems() {
        log.info("Fetching all Food Items from the database");
        return foodItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FoodItemResponse getFoodItemById(Long id) {
        log.info("Fetching Food Item with ID: {} from the database", id);
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new FoodItemNotFoundException("Food Item not found"));
        return mapToResponse(foodItem);
    }

    public FoodItemResponse createFoodItem(FoodItemRequest request) {
        log.info("Creating a new Food Item with Name: {}", request.productName());
        var foodItem = foodItemRepository.save(buildNewFoodItem(request));
        return mapToResponse(foodItem);
    }

    private FoodItemResponse mapToResponse(FoodItem foodItem) {
        return FoodItemResponse.builder()
                .brand(foodItem.getBrand())
                .foodCategory(foodItem.getFoodCategory())
                .foodId(foodItem.getFoodId())
                .productName(foodItem.getProductName())
                .expirationDate(foodItem.getExpirationDate())
                .quantity(foodItem.getQuantity())
                .unit(foodItem.getUnit())
                .brand(foodItem.getBrand())
                .build();
    }

    private FoodItem buildNewFoodItem(FoodItemRequest request) {
        var user = authenticationService.getCurrentUser();
        return FoodItem.builder()
                .foodCategory(request.foodCategory())
                .brand(request.brand())
                .createdAt(LocalDate.now())
                .expirationDate(request.expirationDate())
                .productName(request.productName())
                .quantity(request.quantity())
                .unit(request.unit())
                .user(user)
                .build();
    }
}
