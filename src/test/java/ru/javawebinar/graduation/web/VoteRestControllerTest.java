package ru.javawebinar.graduation.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.service.VoteService;
import ru.javawebinar.graduation.util.VoteTime;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.graduation.RestaurantTestData.getToMatcher;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @AfterEach
    void tearDown() {
        VoteTime.restore();
    }

    //204
    @Test
    void vote() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT1_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isNoContent())
                .andDo(print());

        List<Vote> actual = voteService.getAllByUser_Email(UserTestData.USER1.getEmail());
        assertEquals(1, actual.size());
        Assertions.assertEquals(UserTestData.USER1.getEmail(), actual.get(0).getUserEmail());

        int actualRestaurantId = actual.get(0).getRestaurant().getId();
        Assertions.assertEquals(RestaurantTestData.RESTAURANT1_ID, actualRestaurantId);
    }

    //409
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void voteInvalid() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(post(REST_URL + "for?restaurantId=" + 1)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)))
                .andExpect(status().isConflict());
    }

    //401
    @Test
    void voteAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    //200
    @Test
    void getByRestaurantIdAndDate() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)));
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER2)));
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT3_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER3)));
        perform(get(REST_URL + "by?restaurantId=" + RestaurantTestData.RESTAURANT2_ID + "&date=" + LocalDate.now())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getToMatcher(List.of(RestaurantTestData.VOTE2, RestaurantTestData.VOTE3), Vote.class))
                .andDo(print());
    }

    //200
    @Test
    void getByDate() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)));
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER2)));
        perform(post(REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT3_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER3)));
        perform(get(REST_URL + "by?date=" + LocalDate.now())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getToMatcher(List.of(RestaurantTestData.VOTE2, RestaurantTestData.VOTE3, RestaurantTestData.VOTE4), Vote.class))
                .andDo(print());
    }
}