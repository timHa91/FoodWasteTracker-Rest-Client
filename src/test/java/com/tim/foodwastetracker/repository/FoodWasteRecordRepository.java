package com.tim.foodwastetracker.repository;

import com.tim.foodwastetracker.model.FoodWasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodWasteRecordRepository extends JpaRepository<FoodWasteRecord, Long> {
}
