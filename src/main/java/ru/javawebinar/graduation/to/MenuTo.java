package ru.javawebinar.graduation.to;

import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.graduation.HasId;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.model.MenuItem;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class MenuTo implements HasId {

    protected Integer id;

    @NotNull
    private Integer restaurantId;

    @NotNull
    private List<MenuItem> menuItems;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public MenuTo() {
    }

    public MenuTo(Integer restaurantId, List<MenuItem> menuItems) {
        this.restaurantId = restaurantId;
        this.menuItems = menuItems;
    }

    public MenuTo(Integer id, Integer restaurantId, List<MenuItem> menuItems, LocalDate date) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.menuItems = menuItems;
        this.date = date;
    }

    public static MenuTo fromMenu(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getMenuItems(), menu.getDate());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", menu_items=" + menuItems +
                ", date=" + date +
                '}';
    }
}
