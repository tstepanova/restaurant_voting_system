package ru.javawebinar.graduation.service;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.util.VoteTime;
import ru.javawebinar.graduation.util.exception.VoteException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.graduation.RestaurantTestData.*;

public class VoteServiceImplTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @AfterEach
    void tearDown() {
        VoteTime.restore();
    }

    @Test
    @Transactional
    void vote() {
        RestaurantTestData.increaseVoteTime();
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        Vote stored = service.findByUser_EmailAndDate(UserTestData.USER1.getEmail(), LocalDate.now());
        VOTE_MATCHER_FIELDS_USER_AND_RESTAURANT.assertMatch(stored, RestaurantTestData.VOTE2);
        Restaurant restaurant = (Restaurant) Hibernate.unproxy(stored.getRestaurant());
        RESTAURANT_MATCHER_FIELDS_MENUS_AND_VOTES.assertMatch(restaurant, RestaurantTestData.RESTAURANT2);
        assertEquals(stored.getUserEmail(), UserTestData.USER1.getEmail());
    }

    @Test
    void voteTime() {
        RestaurantTestData.expireVoteTime();
        // first vote
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        // changed mind
        assertThrows(VoteException.class, () ->
                service.vote(RestaurantTestData.RESTAURANT4.getId(), UserTestData.USER1.getEmail()));
    }

    @Test
    void getByRestaurantIdAndDate() {
        RestaurantTestData.increaseVoteTime();
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER2.getEmail());
        service.vote(RestaurantTestData.RESTAURANT3_ID, UserTestData.USER3.getEmail());
        List<Vote> stored = service.getBy(RestaurantTestData.RESTAURANT2_ID, LocalDate.now());
        VOTE_MATCHER.assertMatch(stored, List.of(RestaurantTestData.VOTE2, RestaurantTestData.VOTE3));
    }

    @Test
    void getByDate() {
        RestaurantTestData.increaseVoteTime();
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        service.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER2.getEmail());
        service.vote(RestaurantTestData.RESTAURANT3_ID, UserTestData.USER3.getEmail());
        List<Vote> stored = service.getBy(LocalDate.now());
        VOTE_MATCHER.assertMatch(stored, List.of(RestaurantTestData.VOTE2, RestaurantTestData.VOTE3, RestaurantTestData.VOTE4));
    }
}
