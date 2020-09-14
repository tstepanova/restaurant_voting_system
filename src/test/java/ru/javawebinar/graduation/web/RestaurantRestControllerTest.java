package ru.javawebinar.graduation.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.TestUtil;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.repository.restaurant.JpaRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.graduation.RestaurantTestData.*;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantRestController.REST_URL + '/';

    @Autowired
    protected JpaRestaurantRepository jpaRestaurantRepository;

    @BeforeEach
    void setUp() {
    }

    //200
    @Test
    void getAll() throws Exception {
        perform(get(REST_URL)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.getToMatcher(RestaurantTestData.getAllRestaurants(), Restaurant.class, RestaurantTestData.FIELDS_MENUS_AND_VOTES));
    }

    //200
    @Test
    void getById() throws Exception {
        RESTAURANT2.setMenus(List.of(MENU1));
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_MATCHER.assertMatch(TestUtil.readFromJsonMvcResult(result, Restaurant.class), RestaurantTestData.RESTAURANT2))
                .andDo(print());
    }

    //200
    @Test
    void getByDate() throws Exception {
        perform(get(REST_URL + "by?date=" + LocalDate.now())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES.assertMatch(TestUtil.readListFromJsonMvcResult(result, Restaurant.class), RestaurantTestData.RESTAURANT4))
                .andDo(print());
    }

    //401
    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    //404
    @Test
    void getNotFound() throws Exception {
        perform(get(REST_URL + 1)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Restaurant created = RestaurantTestData.getRestaurant();
        ResultActions action = perform(post(REST_URL)
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(created)));
        Restaurant returned = TestUtil.readFromJson(action, Restaurant.class);
        created.setId(returned.getId());
        RESTAURANT_MATCHER.assertMatch(returned, created);
        List<Restaurant> expectList = List.of(RestaurantTestData.RESTAURANT1, RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3, RestaurantTestData.RESTAURANT4, RestaurantTestData.RESTAURANT5, RestaurantTestData.RESTAURANT6, created);
        RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES.assertMatch(jpaRestaurantRepository.findAll(), expectList);
    }

    //403
    @Test
    void createByUser() throws Exception {
        Restaurant created = RestaurantTestData.getRestaurant();
        perform(post(REST_URL)
                .with(UserTestData.userHttpBasic(UserTestData.USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(created)))
                .andExpect(status().isForbidden());
    }

    //204
    @Test
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getRestaurant();
        updated.setId(RestaurantTestData.RESTAURANT1_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(updated))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES.assertMatch(jpaRestaurantRepository.findById(RestaurantTestData.RESTAURANT1_ID).orElse(null), updated);
    }

    //403
    @Test
    void updateByUser() throws Exception {
        Restaurant updated = RestaurantTestData.getRestaurant();
        updated.setId(RestaurantTestData.RESTAURANT1_ID);

        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(updated))
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    //204
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT3.getId())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isNoContent());
        List<Restaurant> expectList = List.of(RestaurantTestData.RESTAURANT1, RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT4, RestaurantTestData.RESTAURANT5, RestaurantTestData.RESTAURANT6);
        RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES.assertMatch(jpaRestaurantRepository.findAll(), expectList);
    }

    //403
    @Test
    void deleteByUser() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT3.getId())
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    //422
    @Test
    void updateHtmlUnsafe() throws Exception {
        Restaurant invalid = new Restaurant(RestaurantTestData.RESTAURANT1_ID, "Pizza Hut", "<script>alert(911)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(invalid))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    //409
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(RestaurantTestData.RESTAURANT1_ID, "Pizza Hut", "Санкт-Петербург, Обводного канала наб., 120");
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(invalid))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    //409
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(null, "Pizza Hut", "Санкт-Петербург, Обводного канала наб., 120");
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(invalid))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    //422
    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "Pizza Hut", null);
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(invalid))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    //422
    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RestaurantTestData.RESTAURANT1_ID, null, "Санкт-Петербург, Обводного канала наб., 120");
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.writeValue(invalid))
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}