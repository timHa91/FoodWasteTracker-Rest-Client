package com.tim.foodwastetracker.controller;

import com.tim.foodwastetracker.dto.FoodItemRequest;
import com.tim.foodwastetracker.dto.FoodItemResponse;
import com.tim.foodwastetracker.service.FoodItemService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("api/v1/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @ApiOperation(value = "Get all Food Items")
    @GetMapping
    public ResponseEntity<List<FoodItemResponse>> getAllFoodItems() {
        log.info("Received request to get all Food Items");
        return ResponseEntity.ok(foodItemService.getAllFoodItems());
    }

    @ApiOperation(value = "Get a Food Item by its ID")
    @GetMapping("{foodId}")
    public ResponseEntity<FoodItemResponse> getFoodItemById(Long foodId) {
        log.info("Received request to get Food Item with ID: {}", foodId);
        return ResponseEntity.ok(foodItemService.getFoodItemById(foodId));
    }

    @ApiOperation(value = "Create a new Food Item")
    @PostMapping
    public ResponseEntity<FoodItemResponse> createFoodItem(@RequestBody @Valid FoodItemRequest request) {
        log.info("Received request to create a new Food Item with Name: {}", request.productName());
        FoodItemResponse response = foodItemService.createFoodItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
