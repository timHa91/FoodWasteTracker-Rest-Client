package com.tim.foodwastetracker.controller;

import com.tim.foodwastetracker.dto.FoodWasteRecordRequest;
import com.tim.foodwastetracker.dto.FoodWasteRecordResponse;
import com.tim.foodwastetracker.service.FoodWasteRecordService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/records")
@AllArgsConstructor
@Log4j2
public class FoodWasteRecordController {

    private final FoodWasteRecordService foodWasteRecordService;

    @ApiOperation(value = "Get all Food Waste Records")
    @GetMapping
    public ResponseEntity<List<FoodWasteRecordResponse>> getAllRecords() {
        log.info("Received request to get all Food Waste Records");
        return ResponseEntity.ok(foodWasteRecordService.getAllRecords());
    }

    @ApiOperation(value = "Get a Food Waste Record by its ID")
    @GetMapping("{recordId}")
    public ResponseEntity<FoodWasteRecordResponse> getRecordById(@PathVariable Long recordId) {
        log.info("Received request to get Food Waste Record with ID: {}", recordId);
        return ResponseEntity.ok(foodWasteRecordService.getRecordById(recordId));
    }

    @ApiOperation(value = "Create a new Food Waste Record")
    @PostMapping
    public ResponseEntity<FoodWasteRecordResponse> createRecord(@RequestBody @Valid FoodWasteRecordRequest request) {
        log.info("Received request to create a new Food Waste Record for Food Item ID: {}", request.foodItemId());
        FoodWasteRecordResponse response = foodWasteRecordService.createRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
