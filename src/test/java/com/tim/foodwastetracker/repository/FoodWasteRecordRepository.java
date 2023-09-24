package com.tim.foodwastetracker.repository;

import com.tim.foodwastetracker.model.FoodWasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodWasteRecordRepository extends JpaRepository<FoodWasteRecord, Long> {
}
