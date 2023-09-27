package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.dto.FoodWasteRecordRequest;
import com.tim.foodwastetracker.dto.FoodWasteRecordResponse;
import com.tim.foodwastetracker.exception.FoodItemNotFoundException;
import com.tim.foodwastetracker.exception.RecordNotFoundException;
import com.tim.foodwastetracker.model.*;
import com.tim.foodwastetracker.repository.FoodItemRepository;
import com.tim.foodwastetracker.repository.FoodWasteRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodWasteRecordServiceTest {

    @InjectMocks
    private FoodWasteRecordService underTest;
    @Mock
    private FoodWasteRecordRepository foodWasteRecordRepository;
    @Mock
    private FoodItemRepository foodItemRepository;
    @Mock
    private AuthenticationService authenticationService;

    @Test
    void canGetAllRecords() {
        FoodWasteRecord foodWasteRecord1 = new FoodWasteRecord();
        FoodWasteRecord foodWasteRecord2 = new FoodWasteRecord();
        List<FoodWasteRecord> records = Arrays.asList(foodWasteRecord1,foodWasteRecord2);

        when(foodWasteRecordRepository.findAll()).thenReturn(records);

        List<FoodWasteRecordResponse> result = underTest.getAllRecords();

        assertEquals(result.size(), records.size());
    }

    @Test
    void canGetRecordById() {
        Long id = 1L;
        FoodWasteRecord record = new FoodWasteRecord();
        record.setRecordId(id);

        when(foodWasteRecordRepository.findById(id)).thenReturn(Optional.of(record));

        FoodWasteRecordResponse result = underTest.getRecordById(id);

        assertEquals(record.getRecordId(), result.recordId());
    }

    @Test
    void canCreateRecord() {
        Long foodItemId = 1L;
        FoodWasteRecordRequest request = new FoodWasteRecordRequest(
                LocalDate.now(),
                1.0,
                Unit.KILOGRAM,
                WasteReason.ABGELAUFEN,
                "Kitchen",
                foodItemId
        );

        User user = new User();
        when(authenticationService.getCurrentUser()).thenReturn(user);

        FoodItem foodItem = new FoodItem();
        foodItem.setFoodId(foodItemId);
        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(foodItem));

        FoodWasteRecord foodWasteRecord = new FoodWasteRecord();
        foodWasteRecord.setFoodItem(foodItem);

        when(foodWasteRecordRepository.save(any(FoodWasteRecord.class))).thenReturn(foodWasteRecord);

        FoodWasteRecordResponse result = underTest.createRecord(request);

        assertEquals(foodItemId, result.foodItem().getFoodId());
    }

    @Test
    void createRecordThrowsFoodItemNotFound() {
        Long foodItemId = 1L;
        FoodWasteRecordRequest request = new FoodWasteRecordRequest(
                LocalDate.now(),
                1.0,
                Unit.KILOGRAM,
                WasteReason.ABGELAUFEN,
                "Kitchen",
                foodItemId
        );

        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.empty());

        assertThrows(FoodItemNotFoundException.class, () -> underTest.createRecord(request));
    }

    @Test
    void getRecordByIdThrowsRecordNotFound() {
        Long id = 1L;

        when(foodWasteRecordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> underTest.getRecordById(id));
    }

    @Test
    void getAllRecordsThrowsNoRecords() {
        when(foodWasteRecordRepository.findAll()).thenReturn(List.of());

        List<FoodWasteRecordResponse> result = underTest.getAllRecords();

        assertTrue(result.isEmpty());
    }


}