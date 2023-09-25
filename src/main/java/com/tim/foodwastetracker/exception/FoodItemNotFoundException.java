package com.tim.foodwastetracker.exception;

public class FoodItemNotFoundException extends RuntimeException{

    public FoodItemNotFoundException(String message) {
        super(message);
    }
}
