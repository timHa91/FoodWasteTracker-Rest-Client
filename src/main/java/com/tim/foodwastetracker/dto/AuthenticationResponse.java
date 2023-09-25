package com.tim.foodwastetracker.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        String email) {
}
