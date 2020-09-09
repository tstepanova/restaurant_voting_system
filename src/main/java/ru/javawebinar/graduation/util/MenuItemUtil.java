package ru.javawebinar.graduation.util;

import ru.javawebinar.graduation.model.MenuItem;
import ru.javawebinar.graduation.to.MenuItemTo;

public class MenuItemUtil {

    private MenuItemUtil() {
    }

    public static MenuItem createFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(menuItemTo.getId(), menuItemTo.getName(), menuItemTo.getPrice());
    }
}
