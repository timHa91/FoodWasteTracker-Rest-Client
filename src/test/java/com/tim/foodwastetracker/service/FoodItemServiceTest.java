package com.tim.foodwastetracker.service;

import com.tim.foodwastetracker.dto.FoodItemRequest;
import com.tim.foodwastetracker.dto.FoodItemResponse;
import com.tim.foodwastetracker.exception.FoodItemNotFoundException;
import com.tim.foodwastetracker.model.FoodCategory;
import com.tim.foodwastetracker.model.FoodItem;
import com.tim.foodwastetracker.model.Unit;
import com.tim.foodwastetracker.model.User;
import com.tim.foodwastetracker.repository.FoodItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceTest {

    @Mock
    private FoodItemRepository foodItemRepository;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private FoodItemService underTest;

    @Test
    void canGetAllFoodItems() {
        FoodItem foodItem1 = new FoodItem();
        FoodItem foodItem2 = new FoodItem();
        List<FoodItem> foodItems = Arrays.asList(foodItem1, foodItem2);

        when(foodItemRepository.findAll()).thenReturn(foodItems);

        List<FoodItemResponse> result = underTest.getAllFoodItems();

        assertEquals(foodItems.size(), result.size());
    }

    @Test
    void canGetFoodItemById() {
        Long id = 1L;
        FoodItem foodItem = new FoodItem();
        foodItem.setFoodId(id);

        when(foodItemRepository.findById(id)).thenReturn(Optional.of(foodItem));

        FoodItemResponse result = underTest.getFoodItemById(id);

        assertEquals(foodItem.getFoodId(), result.foodId());
    }

    @Test
    void getFoodItemByIdThrowsWhenFoodItemNotFound() {
        Long id = 1L;

        when(foodItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FoodItemNotFoundException.class, () -> underTest.getFoodItemById(id));
    }

    @Test
    void canCreateFoodItem() {
        Long foodId = 1L;
        LocalDate expirationDate = LocalDate.now();
        FoodItemRequest request = new FoodItemRequest(
                "productName",
                FoodCategory.FRUITS,
                expirationDate,
                1.0,
                Unit.KILOGRAM,
                "brand");

        User user = new User();
        when(authenticationService.getCurrentUser()).thenReturn(user);
        FoodItem foodItem = new FoodItem();
        foodItem.setFoodId(foodId);
        foodItem.setExpirationDate(expirationDate);
        when(foodItemRepository.save(any(FoodItem.class))).thenReturn(foodItem);

        FoodItemResponse result = underTest.createFoodItem(request);

        ArgumentCaptor<FoodItem> foodItemArgumentCaptor = ArgumentCaptor.forClass(FoodItem.class);
        verify(foodItemRepository).save(foodItemArgumentCaptor.capture());
        FoodItem capturedFoodItem = foodItemArgumentCaptor.getValue();

        assertEquals(request.productName(), capturedFoodItem.getProductName());
        assertEquals(request.foodCategory(), capturedFoodItem.getFoodCategory());
        assertEquals(request.brand(), capturedFoodItem.getBrand());
        assertEquals(request.quantity(), capturedFoodItem.getQuantity());
        assertEquals(request.unit(), capturedFoodItem.getUnit());
        assertEquals(request.expirationDate(), capturedFoodItem.getExpirationDate());


        assertEquals(result.foodId(), foodId);
        assertEquals(result.expirationDate(), request.expirationDate());
    }
}
