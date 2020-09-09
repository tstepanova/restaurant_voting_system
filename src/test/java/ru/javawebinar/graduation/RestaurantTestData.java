package ru.javawebinar.graduation;

import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.javawebinar.graduation.model.*;
import ru.javawebinar.graduation.to.MenuTo;
import ru.javawebinar.graduation.util.VoteTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.graduation.UserTestData.*;
import static ru.javawebinar.graduation.web.json.JacksonObjectMapper.getMapper;

public class RestaurantTestData {
    public static TestMatcher<Menu> MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS = TestMatcher.usingFieldsWithIgnoringAssertions(Menu.class, "restaurant", "menuItems");

    public static TestMatcher<MenuTo> MENU_TO_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS = TestMatcher.usingFieldsWithIgnoringAssertions(MenuTo.class, "restaurant", "menuItems");

    public static TestMatcher<MenuItem> MENU_ITEM_MATCHER_FIELD_MENU = TestMatcher.usingFieldsWithIgnoringAssertions(MenuItem.class, "menu");
    public static TestMatcher<MenuItem> MENU_ITEM_MATCHER_FIELDS_ID_AND_MENU = TestMatcher.usingFieldsWithIgnoringAssertions(MenuItem.class, "id", "menu");

    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class);
    public static TestMatcher<Vote> VOTE_MATCHER_FIELDS_USER_AND_RESTAURANT = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "user", "restaurant");

    public static TestMatcher<Restaurant> RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES = TestMatcher.usingFieldsWithIgnoringAssertions(Restaurant.class, "menus", "votes");
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Restaurant.class);

    public static final int RESTAURANT1_ID = AbstractBaseEntity.START_SEQ + 4;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static final int RESTAURANT3_ID = RESTAURANT1_ID + 2;
    public static final int RESTAURANT4_ID = RESTAURANT1_ID + 3;
    public static final int RESTAURANT5_ID = RESTAURANT1_ID + 4;
    public static final int RESTAURANT6_ID = RESTAURANT1_ID + 5;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Аль-Шарк", "Санкт-Петербург, Литейный пр., 43");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Pizza Hut", "Санкт-Петербург, Обводного канала наб., 120");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Две палочки", "Санкт-Петербург, Просвещения пр., 19");
    public static final Restaurant RESTAURANT4 = new Restaurant(RESTAURANT4_ID, "Лимончелло", "Санкт-Петербург, пр. Литейный, 40");
    public static final Restaurant RESTAURANT5 = new Restaurant(RESTAURANT5_ID, "Orange-Club", "Санкт-Петербург, Сенная площадь, 2");
    public static final Restaurant RESTAURANT6 = new Restaurant(RESTAURANT6_ID, "Барашки", "Санкт-Петербург, Сенная площадь, 2");

    public static final String[] FIELDS_MENUS_AND_VOTES = new String[]{"menus", "votes"};

    public static final int MENU_ITEM1_ID = AbstractBaseEntity.START_SEQ + 13;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM1_ID, "Хат ролл с курицей", BigDecimal.valueOf(159).setScale(2));
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM1_ID + 1, "Запеченные картофельные дольки", BigDecimal.valueOf(149.50).setScale(2));
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM1_ID + 2, "Пепси", BigDecimal.valueOf(99).setScale(2));

    public static final MenuItem MENU_ITEM_4 = new MenuItem(MENU_ITEM1_ID + 3, "Крем-суп с лососем", BigDecimal.valueOf(135).setScale(2));
    public static final MenuItem MENU_ITEM_5 = new MenuItem(MENU_ITEM1_ID + 4, "Салат Цезарь", BigDecimal.valueOf(120).setScale(2));
    public static final MenuItem MENU_ITEM_6 = new MenuItem(MENU_ITEM1_ID + 5, "Морс ягодный", BigDecimal.valueOf(65).setScale(2));

    public static final MenuItem MENU_ITEM_7 = new MenuItem(MENU_ITEM1_ID + 6, "Картофельный крем-суп с беконом", BigDecimal.valueOf(180).setScale(2));
    public static final MenuItem MENU_ITEM_8 = new MenuItem(MENU_ITEM1_ID + 7, "Паста Феттучине Болонезе", BigDecimal.valueOf(110).setScale(2));
    public static final MenuItem MENU_ITEM_9 = new MenuItem(MENU_ITEM1_ID + 8, "Мини Фокаччо", BigDecimal.valueOf(35).setScale(2));
    public static final MenuItem MENU_ITEM_10 = new MenuItem(MENU_ITEM1_ID + 9, "Американо", BigDecimal.valueOf(70).setScale(2));

    public static final String FIELD_ID = "id";
    public static final String FIELD_MENU = "menu";

    public static final int MENU1_ID = AbstractBaseEntity.START_SEQ + 10;

    public static final Menu MENU1 = new Menu(MENU1_ID, LocalDate.of(2020, 8, 31), List.of(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_3), RESTAURANT2);
    public static final Menu MENU2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, 8, 31), List.of(MENU_ITEM_4, MENU_ITEM_5, MENU_ITEM_6), RESTAURANT3);
    public static final Menu MENU3 = new Menu(MENU1_ID + 2, LocalDate.now(), List.of(MENU_ITEM_7, MENU_ITEM_8, MENU_ITEM_9, MENU_ITEM_10), RESTAURANT4);

    public static final String[] FIELDS_RESTAURANT_AND_MENU_ITEMS = new String[]{"restaurant", "menuItems"};

    public static final int VOTE1_ID = AbstractBaseEntity.START_SEQ + 23;
    public static final int VOTE2_ID = VOTE1_ID + 1;
    public static final int VOTE3_ID = VOTE2_ID + 1;
    public static final int VOTE4_ID = VOTE3_ID + 1;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, LocalDate.of(2020, 8, 31), USER1.getEmail(), RESTAURANT2);
    public static final Vote VOTE2 = new Vote(VOTE2_ID, USER1.getEmail(), RESTAURANT2);
    public static final Vote VOTE3 = new Vote(VOTE3_ID, USER2.getEmail(), RESTAURANT2);
    public static final Vote VOTE4 = new Vote(VOTE4_ID, USER3.getEmail(), RESTAURANT3);

    public static Restaurant getRestaurant() {
        return new Restaurant(null, "Евразия", "Санкт-Петербург, Невский пр., 175");
    }

    public static List<Restaurant> getAllRestaurants(Restaurant... restaurants) {
        List<Restaurant> restaurantList = new ArrayList<>(List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3, RESTAURANT4, RESTAURANT5, RESTAURANT6));
        if (restaurants.length > 0) restaurantList.addAll(Arrays.asList(restaurants));
        return restaurantList;
    }

    public static List<Menu> getAllMenus(Menu... menus) {
        List<Menu> menuList = new ArrayList<>((List.of(MENU1, MENU2, MENU3)));
        if (menus.length > 0) menuList.addAll(Arrays.asList(menus));
        return menuList;
    }

    public static List<MenuTo> getAllMenuTo() {
        return List.of(MENU1, MENU2, MENU3).stream()
                .map(MenuTo::fromMenu)
                .collect(Collectors.toList());
    }

    public static List<MenuItem> getNewMenuItems() {
        return List.of(new MenuItem("Мисо суп", BigDecimal.valueOf(90).setScale(2)), new MenuItem("Ролл Калифорния", BigDecimal.valueOf(160).setScale(2)));
    }

    public static void expireVoteTime() {
        VoteTime.setTime(LocalTime.now().minusSeconds(1));
    }

    public static void increaseVoteTime() {
        VoteTime.setTime(LocalTime.now().plusSeconds(1));
    }

    public static <T> ResultMatcher getToMatcher(Iterable<T> expected, Class<T> clazz, String... ignoringFields) {
        return result -> assertThat(readListFromJsonMvcResult(result, clazz)).usingElementComparatorIgnoringFields(ignoringFields).isEqualTo(expected);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return readValues(TestUtil.getContent(result), clazz);
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = getMapper().readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }
}
