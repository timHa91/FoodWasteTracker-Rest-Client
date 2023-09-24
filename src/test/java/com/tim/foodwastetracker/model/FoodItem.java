package com.tim.foodwastetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;
    @ManyToOne
    private User user;
    private String productName;
    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;
    private Instant expirationDate;
    private Instant createdAt;
    private Instant updatedAt;
    private Long quantity;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    private String brand;
}
