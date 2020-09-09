package ru.javawebinar.graduation.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.TestUtil;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.repository.JpaMenuRepository;
import ru.javawebinar.graduation.to.MenuTo;
import ru.javawebinar.graduation.util.MenuUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.graduation.RestaurantTestData.MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS;
import static ru.javawebinar.graduation.RestaurantTestData.MENU_TO_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS;

class MenuRestControllerTest extends AbstractControllerTest {

    @Autowired
    private JpaMenuRepository jpaMenuRepository;

    private static final String REST_URL = MenuRestController.REST_URL + '/';

    //200
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.contentJson(RestaurantTestData.getAllMenuTo()))
                .andDo(print());
    }

    //201
    @Test
    void create() throws Exception {
        MenuTo createdTo = new MenuTo(RestaurantTestData.RESTAURANT1_ID, RestaurantTestData.getNewMenuItems());

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1))
                .content(TestUtil.writeValue(createdTo)))
                .andExpect(status().isCreated())
                .andDo(print());

        Menu returned = TestUtil.readFromJson(action, Menu.class);
        Menu created = MenuUtil.createFromTo(createdTo);

        created.setId(returned.getId());

        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.contentJson(returned, created);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(jpaMenuRepository.getById(returned.getId()), created);
    }

    //422
    @Test
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(RestaurantTestData.RESTAURANT1_ID, null);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1))
                .content(TestUtil.writeValue(invalid)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    //403
    @Test
    void createByUser() throws Exception {
        MenuTo createdTo = new MenuTo(RestaurantTestData.RESTAURANT1_ID, RestaurantTestData.getNewMenuItems());

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.USER1))
                .content(TestUtil.writeValue(createdTo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    //200
    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.MENU1_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(TestUtil.readFromJsonMvcResult(result, Menu.class), RestaurantTestData.MENU1))
                .andDo(print());
    }

    //422
    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isUnprocessableEntity());
    }

    //401
    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    //200
    @Test
    void update() throws Exception {
        MenuTo updatedTo = new MenuTo(RestaurantTestData.MENU1_ID, RestaurantTestData.RESTAURANT1_ID, RestaurantTestData.getNewMenuItems(), RestaurantTestData.MENU1.getDate());

        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1))
                .content(TestUtil.writeValue(updatedTo)))
                .andExpect(status().isOk())
                .andDo(print());

        Menu returned = TestUtil.readFromJson(action, Menu.class);
        Menu created = MenuUtil.createFromTo(updatedTo);

        created.setId(returned.getId());

        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(returned, created);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(jpaMenuRepository.getById(returned.getId()), created);
    }

    //422
    @Test
    void updateInvalid() throws Exception {
        MenuTo invalid = new MenuTo(RestaurantTestData.MENU1_ID, RestaurantTestData.RESTAURANT1_ID, null, RestaurantTestData.MENU1.getDate());

        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1))
                .content(TestUtil.writeValue(invalid)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    //403
    @Test
    void updateForbidden() throws Exception {
        MenuTo updatedTo = new MenuTo(RestaurantTestData.MENU1_ID, RestaurantTestData.RESTAURANT1_ID, RestaurantTestData.getNewMenuItems(), RestaurantTestData.MENU1.getDate());

        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(UserTestData.userHttpBasic(UserTestData.USER1))
                .content(TestUtil.writeValue(updatedTo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    //204
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.MENU1_ID)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isNoContent());
        List<Menu> expectList = List.of(RestaurantTestData.MENU2, RestaurantTestData.MENU3);
        MENU_MATCHER_FIELDS_RESTAURANT_AND_MENU_ITEMS.assertMatch(jpaMenuRepository.findAll(), expectList);
    }

    //403
    @Test
    void deleteByUser() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.MENU1_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}