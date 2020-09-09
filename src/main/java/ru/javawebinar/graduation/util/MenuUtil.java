package ru.javawebinar.graduation.util;

import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;

public class MenuUtil {

    private MenuUtil() {
    }

    public static Menu createFromTo(MenuTo menuTo) {
        Menu menu = new Menu(menuTo.getId(), menuTo.getDate() == null ? LocalDate.now() : menuTo.getDate(), new ArrayList<>(), null);
        menuTo.getMenuItems().forEach(menu::addMenuItem);
        return menu;
    }

    public static MenuTo asTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getMenuItems(), menu.getDate());
    }
}
