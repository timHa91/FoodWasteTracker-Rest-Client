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
public class FoodWasteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;
    private Instant wasteDate;
    private Long quantity;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    @Enumerated(EnumType.STRING)
    private WasteReason reason;
    private String location;
    private Instant createdAt;
    @OneToOne
    FoodItem foodItem;
    @ManyToOne
    User user;
}
