package com.tim.foodwastetracker.dto;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
public record RegisterRequest(
        @NotBlank(message = "First name cannot be blank")
        String firstname,
        @NotBlank(message = "Last name cannot be blank")
        String lastname,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password) {
}
