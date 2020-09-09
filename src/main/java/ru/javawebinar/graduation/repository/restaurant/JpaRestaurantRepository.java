package ru.javawebinar.graduation.repository.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Restaurant;

public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Integer>, RestaurantRepository {

    @Transactional
    @Modifying
    int removeById(int id);
}