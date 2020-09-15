package ru.javawebinar.graduation.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.model.Voting;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.graduation.RestaurantTestData.getToMatcher;

class VotingRestControllerTest extends AbstractControllerTest {

    private static final String VOTING_REST_URL = VotingRestController.VOTING_REST_URL + '/';
    private static final String VOTES_REST_URL = VoteRestController.VOTES_REST_URL + '/';

    //401
    @Test
    void votingAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTING_REST_URL + "by?date=" + LocalDate.now()))
                .andExpect(status().isUnauthorized());
    }

    //200
    @Test
    void getByRestaurantIdAndDate() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)));
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER2)));
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT3_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER3)));
        perform(MockMvcRequestBuilders.get(VOTING_REST_URL + "by?restaurantId=" + RestaurantTestData.RESTAURANT2_ID + "&date=" + LocalDate.now())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getToMatcher(List.of(RestaurantTestData.VOTING_RESTAURANT2), Voting.class))
                .andDo(print());
    }

    //200
    @Test
    void getByDate() throws Exception {
        RestaurantTestData.increaseVoteTime();
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER1)));
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT2_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER2)));
        perform(MockMvcRequestBuilders.put(VOTES_REST_URL + "for?restaurantId=" + RestaurantTestData.RESTAURANT3_ID)
                .with(UserTestData.userHttpBasic(UserTestData.USER3)));
        perform(MockMvcRequestBuilders.get(VOTING_REST_URL + "by?date=" + LocalDate.now())
                .with(UserTestData.userHttpBasic(UserTestData.ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getToMatcher(List.of(RestaurantTestData.VOTING_RESTAURANT2, RestaurantTestData.VOTING_RESTAURANT3), Voting.class))
                .andDo(print());
    }
}