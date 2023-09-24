package com.tim.foodwastetracker.repository;

import com.tim.foodwastetracker.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
