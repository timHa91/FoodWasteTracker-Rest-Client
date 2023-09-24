package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.repository.FoodWasteRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodWasteRecordService {

    private final FoodWasteRecordRepository foodWasteRecordRepository;
}
