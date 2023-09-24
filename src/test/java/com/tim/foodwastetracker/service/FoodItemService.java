package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.repository.FoodItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
}
