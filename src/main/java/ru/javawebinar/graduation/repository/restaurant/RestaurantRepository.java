package ru.javawebinar.graduation.repository.restaurant;

import ru.javawebinar.graduation.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant getById(int id);

    List<Restaurant> getByDate(LocalDate date);
}
