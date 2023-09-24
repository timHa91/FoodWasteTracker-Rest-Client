package com.tim.foodwastetracker.repository;

import com.tim.foodwastetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
