package ru.javawebinar.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.repository.JpaMenuRepository;
import ru.javawebinar.graduation.repository.restaurant.JpaRestaurantRepository;
import ru.javawebinar.graduation.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.graduation.util.ValidationUtil.checkNew;

@Service
public class MenuService {

    private final JpaRestaurantRepository jpaRestaurantRepository;
    private final JpaMenuRepository jpaMenuRepository;

    @Autowired
    public MenuService(JpaRestaurantRepository jpaRestaurantRepository, JpaMenuRepository jpaMenuRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
        this.jpaMenuRepository = jpaMenuRepository;
    }

    @Transactional
    public Menu create(Menu menu, int restaurantId) {
        checkNew(menu);
        Restaurant restaurant = jpaRestaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Not found restaurant with id=" + restaurantId)
        );
        menu.setRestaurant(restaurant);
        return jpaMenuRepository.save(menu);

    }

    public Menu getById(int id) {
        return jpaMenuRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found menu with id=" + id));
    }

    public List<Menu> getBy(int restaurantId, LocalDate date) {
        if (date == null) return jpaMenuRepository.getByRestaurant_Id(restaurantId);
        return jpaMenuRepository.getByRestaurant_IdAndDate(restaurantId, date);
    }

    public List<Menu> getBy(LocalDate date) {
        return jpaMenuRepository.getByDate(date);
    }

    @Transactional
    public Menu update(Menu menu, int restaurantId) {
        jpaMenuRepository.findById(menu.getId()).orElseThrow(() -> new NotFoundException("Not found menu with id=" + menu.getId()));
        Restaurant restaurant = jpaRestaurantRepository.getOne(restaurantId);
        menu.setRestaurant(restaurant);
        return jpaMenuRepository.save(menu);
    }

    public void delete(int id) {
        jpaMenuRepository.deleteById(id);
    }

    public List<Menu> getAll() {
        return jpaMenuRepository.getAllByOrderByIdAsc();
    }
}
