package ru.javawebinar.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.util.exception.IllegalRequestDataException;
import ru.javawebinar.graduation.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.graduation.RestaurantTestData.*;


class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        Menu created = new Menu(null, LocalDate.now(), new ArrayList<>(), RestaurantTestData.RESTAURANT1);
        RestaurantTestData.getNewMenuItems().forEach(created::addMenuItem);
        menuService.create(created, RestaurantTestData.RESTAURANT1_ID);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(menuService.getAll(), List.of(RestaurantTestData.MENU1, RestaurantTestData.MENU2, RestaurantTestData.MENU3, created));
    }

    @Test
    void createNotFound() throws Exception {
        Menu created = new Menu(null, LocalDate.now(), RestaurantTestData.getNewMenuItems(), RestaurantTestData.RESTAURANT1);
        assertThrows(NotFoundException.class, () -> menuService.create(created, 1));
    }

    @Test
    void createWithId() throws Exception {
        Menu created = new Menu(1, LocalDate.now(), RestaurantTestData.getNewMenuItems(), RestaurantTestData.RESTAURANT1);
        assertThrows(IllegalRequestDataException.class, () -> menuService.create(created, RestaurantTestData.RESTAURANT1_ID));
    }

    @Test
    void getById() {
        Menu actual = menuService.getById(RestaurantTestData.MENU1_ID);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(actual, RestaurantTestData.MENU1);
        MENU_ITEM_MATCHER_FIELD_MENU.assertMatch(actual.getMenuItems(), RestaurantTestData.MENU1.getMenuItems());
    }

    @Test
    void getByRestaurantId() {
        List<Menu> actual = menuService.getBy(RestaurantTestData.RESTAURANT3.getId(), null);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(actual, List.of(RestaurantTestData.MENU2));
    }

    @Test
    void getByRestaurantIdAndDate() {
        List<Menu> actual = menuService.getBy(RestaurantTestData.RESTAURANT4.getId(), LocalDate.now());
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(actual, List.of(RestaurantTestData.MENU3));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void update() {
        Menu updated = new Menu(RestaurantTestData.MENU3.getId(), LocalDate.now(), new ArrayList<>(), RestaurantTestData.RESTAURANT5);
        RestaurantTestData.getNewMenuItems().forEach(updated::addMenuItem);
        Menu actual = menuService.update(updated, RestaurantTestData.RESTAURANT5.getId());
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(actual, updated);
        MENU_ITEM_MATCHER_FIELDS_ID_AND_MENU.assertMatch(actual.getMenuItems(), updated.getMenuItems());
    }

    @Test
    void updateNotFound() throws Exception {
        Menu updated = new Menu(1, LocalDate.now(), RestaurantTestData.getNewMenuItems(), RestaurantTestData.RESTAURANT3);
        assertThrows(NotFoundException.class, () -> menuService.update(updated, RestaurantTestData.RESTAURANT3.getId()));
    }

    @Test
    void delete() {
        menuService.delete(RestaurantTestData.MENU1_ID);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(menuService.getAll(), List.of(RestaurantTestData.MENU2, RestaurantTestData.MENU3));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(EmptyResultDataAccessException.class, () -> menuService.delete(1));
    }

    @Test
    void getAll() {
        List<Menu> actual = menuService.getAll();
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(actual, RestaurantTestData.getAllMenus());
    }
}