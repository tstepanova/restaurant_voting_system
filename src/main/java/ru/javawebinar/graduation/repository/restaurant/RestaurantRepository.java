package ru.javawebinar.graduation.repository.restaurant;

import ru.javawebinar.graduation.model.Restaurant;

import java.time.LocalDate;

public interface RestaurantRepository {

    Restaurant getByIdWithMenuByDate(int id, LocalDate date);
}
